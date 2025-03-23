<template>
    <div class="app-container">
        <el-card>
            <div slot="header">
                <span>安全隐患排查治理</span>
            </div>

            <el-form :inline="true" size="mini" @submit.native.prevent>
                <el-form-item>
                    <el-input v-model.trim="search.description" placeholder="隐患描述" clearable/>
                </el-form-item>
                <el-form-item>
                    <el-select v-model="search.type" placeholder="隐患类型" clearable>
                        <el-option label="设备隐患" value="equipment"/>
                        <el-option label="消防隐患" value="fire"/>
                        <el-option label="电气隐患" value="electrical"/>
                        <el-option label="化学品隐患" value="chemical"/>
                        <el-option label="环境隐患" value="environment"/>
                        <el-option label="操作隐患" value="operation"/>
                        <el-option label="其他隐患" value="other"/>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-select v-model="search.level" placeholder="隐患级别" clearable>
                        <el-option label="一般隐患" value="normal"/>
                        <el-option label="重大隐患" value="serious"/>
                        <el-option label="危急隐患" value="critical"/>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-select v-model="search.status" placeholder="整改状态" clearable>
                        <el-option label="待整改" value="pending"/>
                        <el-option label="整改中" value="processing"/>
                        <el-option label="已整改" value="completed"/>
                        <el-option label="已验收" value="accepted"/>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" @click="refresh(true)">查询</el-button>
                    <el-button v-if="canAdd" type="success" icon="el-icon-plus" @click="add">新增</el-button>
                </el-form-item>
            </el-form>

            <el-table v-loading="loading" :data="data" border>
                <el-table-column label="编号" prop="id" width="80" align="center"/>
                <el-table-column label="隐患描述" prop="description" min-width="180" show-overflow-tooltip/>
                <el-table-column label="隐患类型" prop="type" width="100" align="center">
                    <template slot-scope="{row}">
                        {{getTypeText(row.type)}}
                    </template>
                </el-table-column>
                <el-table-column label="隐患级别" prop="level" width="100" align="center">
                    <template slot-scope="{row}">
                        <el-tag :type="getLevelType(row.level)">{{getLevelText(row.level)}}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="发现部门" prop="department" min-width="100"/>
                <el-table-column label="发现时间" prop="foundTime" width="120" align="center"/>
                <el-table-column label="整改期限" prop="deadline" width="120" align="center"/>
                <el-table-column label="整改状态" prop="status" width="100" align="center">
                    <template slot-scope="{row}">
                        <el-tag :type="getStatusType(row.status)">{{getStatusText(row.status)}}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="整改责任人" prop="responsiblePerson" min-width="100"/>
                <el-table-column label="操作" width="240" align="center" fixed="right">
                    <template slot-scope="{row}">
                        <el-button v-if="canView" type="text" size="mini" @click="view(row)">查看</el-button>
                        <el-button v-if="canUpdate" type="text" size="mini" @click="edit(row)">编辑</el-button>
                        <el-button v-if="canDelete" type="text" size="mini" @click="del(row)">删除</el-button>
                        <el-button v-if="canUpdateStatus && row.status === 'pending'" type="text" size="mini" @click="startRectification(row)">开始整改</el-button>
                        <el-button v-if="canUpdateStatus && row.status === 'processing'" type="text" size="mini" @click="completeRectification(row)">完成整改</el-button>
                        <el-button v-if="canUpdateStatus && row.status === 'completed'" type="text" size="mini" @click="acceptRectification(row)">验收</el-button>
                    </template>
                </el-table-column>
            </el-table>

            <el-pagination
                v-if="total > 0"
                :current-page.sync="page.current"
                :page-size.sync="page.size"
                :total="total"
                layout="total, sizes, prev, pager, next, jumper"
                :page-sizes="[10, 20, 30, 50]"
                class="pagination"
                @size-change="refresh()"
                @current-change="refresh()"
            />
        </el-card>

        <!-- 添加/编辑隐患弹窗 -->
        <el-dialog
            :title="dialogType === 'add' ? '新增隐患' : '编辑隐患'"
            :visible.sync="dialogVisible"
            width="650px"
            @closed="closeDialog"
        >
            <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="隐患描述" prop="description">
                    <el-input v-model.trim="form.description" type="textarea" :rows="2" placeholder="请输入隐患描述"/>
                </el-form-item>
                <el-form-item label="隐患类型" prop="type">
                    <el-select v-model="form.type" placeholder="请选择隐患类型" style="width: 100%">
                        <el-option label="设备隐患" value="equipment"/>
                        <el-option label="消防隐患" value="fire"/>
                        <el-option label="电气隐患" value="electrical"/>
                        <el-option label="化学品隐患" value="chemical"/>
                        <el-option label="环境隐患" value="environment"/>
                        <el-option label="操作隐患" value="operation"/>
                        <el-option label="其他隐患" value="other"/>
                    </el-select>
                </el-form-item>
                <el-form-item label="隐患级别" prop="level">
                    <el-select v-model="form.level" placeholder="请选择隐患级别" style="width: 100%">
                        <el-option label="一般隐患" value="normal"/>
                        <el-option label="重大隐患" value="serious"/>
                        <el-option label="危急隐患" value="critical"/>
                    </el-select>
                </el-form-item>
                <el-form-item label="发现部门" prop="department">
                    <el-input v-model.trim="form.department" placeholder="请输入发现部门"/>
                </el-form-item>
                <el-form-item label="发现人" prop="foundBy">
                    <el-input v-model.trim="form.foundBy" placeholder="请输入发现人"/>
                </el-form-item>
                <el-form-item label="发现时间" prop="foundTime">
                    <el-date-picker
                        v-model="form.foundTime"
                        type="datetime"
                        placeholder="选择发现时间"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        style="width: 100%"
                    />
                </el-form-item>
                <el-form-item label="整改期限" prop="deadline">
                    <el-date-picker
                        v-model="form.deadline"
                        type="date"
                        placeholder="选择整改期限"
                        value-format="yyyy-MM-dd"
                        style="width: 100%"
                    />
                </el-form-item>
                <el-form-item label="整改责任人" prop="responsiblePerson">
                    <el-input v-model.trim="form.responsiblePerson" placeholder="请输入整改责任人"/>
                </el-form-item>
                <el-form-item label="责任部门" prop="responsibleDept">
                    <el-input v-model.trim="form.responsibleDept" placeholder="请输入责任部门"/>
                </el-form-item>
                <el-form-item label="整改措施" prop="rectificationMeasure">
                    <el-input v-model.trim="form.rectificationMeasure" type="textarea" :rows="3" placeholder="请输入整改措施"/>
                </el-form-item>
                <el-form-item label="隐患照片">
                    <el-upload
                        class="upload-demo"
                        action="#"
                        :auto-upload="false"
                        :on-change="handlePhotoChange"
                        :limit="5"
                        :file-list="photoList"
                        multiple
                    >
                        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                        <div slot="tip" class="el-upload__tip">支持jpg、png、jpeg格式图片</div>
                    </el-upload>
                </el-form-item>
            </el-form>
            <div slot="footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" :loading="submitting" @click="submit">确定</el-button>
            </div>
        </el-dialog>

        <!-- 状态更新弹窗 -->
        <el-dialog
            :title="statusDialogTitle"
            :visible.sync="statusDialogVisible"
            width="550px"
            @closed="closeStatusDialog"
        >
            <el-form ref="statusForm" :model="statusForm" :rules="statusRules" label-width="100px">
                <el-form-item v-if="currentStatus === 'pending'" label="开始时间" prop="startTime">
                    <el-date-picker
                        v-model="statusForm.startTime"
                        type="datetime"
                        placeholder="选择开始整改时间"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        style="width: 100%"
                    />
                </el-form-item>
                <el-form-item v-if="currentStatus === 'processing'" label="完成时间" prop="completeTime">
                    <el-date-picker
                        v-model="statusForm.completeTime"
                        type="datetime"
                        placeholder="选择整改完成时间"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        style="width: 100%"
                    />
                </el-form-item>
                <el-form-item v-if="currentStatus === 'completed'" label="验收时间" prop="acceptTime">
                    <el-date-picker
                        v-model="statusForm.acceptTime"
                        type="datetime"
                        placeholder="选择验收时间"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        style="width: 100%"
                    />
                </el-form-item>
                <el-form-item v-if="currentStatus === 'completed'" label="验收人" prop="acceptBy">
                    <el-input v-model.trim="statusForm.acceptBy" placeholder="请输入验收人"/>
                </el-form-item>
                <el-form-item label="具体说明" prop="description">
                    <el-input v-model.trim="statusForm.description" type="textarea" :rows="3" placeholder="请输入具体说明"/>
                </el-form-item>
                <el-form-item label="相关照片">
                    <el-upload
                        class="upload-demo"
                        action="#"
                        :auto-upload="false"
                        :on-change="handleStatusPhotoChange"
                        :limit="5"
                        :file-list="statusPhotoList"
                        multiple
                    >
                        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                        <div slot="tip" class="el-upload__tip">支持jpg、png、jpeg格式图片</div>
                    </el-upload>
                </el-form-item>
            </el-form>
            <div slot="footer">
                <el-button @click="statusDialogVisible = false">取消</el-button>
                <el-button type="primary" :loading="submitting" @click="submitStatusUpdate">确定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {wic} from "@/util/auth"
import {elError, elSuccess, elConfirm} from "@/util/message"

export default {
    name: "hazardManagement",

    data() {
        return {
            loading: false,
            submitting: false,
            
            search: {
                description: '',
                type: '',
                level: '',
                status: ''
            },
            
            data: [], // 表格数据
            total: 0,
            page: {
                current: 1,
                size: 10
            },
            
            dialogType: 'add', // 'add' 或 'edit'
            dialogVisible: false,
            form: {
                id: null,
                description: '',
                type: '',
                level: '',
                department: '',
                foundBy: '',
                foundTime: '',
                deadline: '',
                responsiblePerson: '',
                responsibleDept: '',
                rectificationMeasure: '',
                status: 'pending'
            },
            photoList: [],
            photos: [],
            
            // 状态更新相关
            statusDialogVisible: false,
            statusDialogTitle: '',
            currentStatus: '',
            statusForm: {
                id: null,
                startTime: '',
                completeTime: '',
                acceptTime: '',
                acceptBy: '',
                description: ''
            },
            statusPhotoList: [],
            statusPhotos: [],
            
            rules: {
                description: [
                    { required: true, message: '请输入隐患描述', trigger: 'blur' }
                ],
                type: [
                    { required: true, message: '请选择隐患类型', trigger: 'change' }
                ],
                level: [
                    { required: true, message: '请选择隐患级别', trigger: 'change' }
                ],
                department: [
                    { required: true, message: '请输入发现部门', trigger: 'blur' }
                ],
                foundBy: [
                    { required: true, message: '请输入发现人', trigger: 'blur' }
                ],
                foundTime: [
                    { required: true, message: '请选择发现时间', trigger: 'change' }
                ],
                deadline: [
                    { required: true, message: '请选择整改期限', trigger: 'change' }
                ],
                responsiblePerson: [
                    { required: true, message: '请输入整改责任人', trigger: 'blur' }
                ],
                responsibleDept: [
                    { required: true, message: '请输入责任部门', trigger: 'blur' }
                ],
                rectificationMeasure: [
                    { required: true, message: '请输入整改措施', trigger: 'blur' }
                ]
            },
            
            statusRules: {
                startTime: [
                    { required: true, message: '请选择开始整改时间', trigger: 'change' }
                ],
                completeTime: [
                    { required: true, message: '请选择整改完成时间', trigger: 'change' }
                ],
                acceptTime: [
                    { required: true, message: '请选择验收时间', trigger: 'change' }
                ],
                acceptBy: [
                    { required: true, message: '请输入验收人', trigger: 'blur' }
                ],
                description: [
                    { required: true, message: '请输入具体说明', trigger: 'blur' }
                ]
            }
        }
    },

    computed: {
        // 权限控制
        ...wic({
            add: {request: () => Promise.resolve({})},
            update: {request: () => Promise.resolve({})},
            del: {request: () => Promise.resolve({})},
            updateStatus: {request: () => Promise.resolve({})}
        }),
        canView() { return true },
        canUpdateStatus() { return true }
    },

    created() {
        this.refresh()
    },

    methods: {
        // 刷新数据
        refresh(reset = false) {
            if (reset) this.page.current = 1
            this.loading = true
            
            // 模拟获取数据，实际应该调用接口
            setTimeout(() => {
                this.loading = false
                // 模拟数据
                this.data = [
                    {
                        id: '001',
                        description: '车间1号区域消防通道被杂物堵塞',
                        type: 'fire',
                        level: 'serious',
                        department: '安全部',
                        foundBy: '张三',
                        foundTime: '2023-12-10 14:35:00',
                        deadline: '2023-12-15',
                        responsiblePerson: '李四',
                        responsibleDept: '生产部',
                        rectificationMeasure: '清理消防通道杂物，划定消防通道标识线，设置警示牌，加强巡查',
                        status: 'processing'
                    },
                    {
                        id: '002',
                        description: '车间2号生产线设备漏油',
                        type: 'equipment',
                        level: 'normal',
                        department: '生产部',
                        foundBy: '王五',
                        foundTime: '2023-12-12 09:20:00',
                        deadline: '2023-12-20',
                        responsiblePerson: '赵六',
                        responsibleDept: '设备部',
                        rectificationMeasure: '更换设备密封圈，加强日常设备维护保养',
                        status: 'pending'
                    }
                ]
                this.total = 2
            }, 500)
        },

        // 获取隐患类型文本
        getTypeText(type) {
            const types = {
                'equipment': '设备隐患',
                'fire': '消防隐患',
                'electrical': '电气隐患',
                'chemical': '化学品隐患',
                'environment': '环境隐患',
                'operation': '操作隐患',
                'other': '其他隐患'
            }
            return types[type] || '未知类型'
        },

        // 获取隐患级别类型
        getLevelType(level) {
            const types = {
                normal: 'info',
                serious: 'warning',
                critical: 'danger'
            }
            return types[level] || 'info'
        },

        // 获取隐患级别文本
        getLevelText(level) {
            const texts = {
                normal: '一般隐患',
                serious: '重大隐患',
                critical: '危急隐患'
            }
            return texts[level] || '未知级别'
        },

        // 获取状态类型
        getStatusType(status) {
            const types = {
                pending: 'danger',
                processing: 'warning',
                completed: 'info',
                accepted: 'success'
            }
            return types[status] || 'info'
        },

        // 获取状态文本
        getStatusText(status) {
            const texts = {
                pending: '待整改',
                processing: '整改中',
                completed: '已整改',
                accepted: '已验收'
            }
            return texts[status] || '未知'
        },

        // 新增隐患
        add() {
            this.dialogType = 'add'
            this.form.foundTime = new Date().toISOString().replace('T', ' ').substr(0, 19)
            this.dialogVisible = true
        },

        // 编辑隐患
        edit(row) {
            this.dialogType = 'edit'
            this.form = { ...row }
            this.dialogVisible = true
        },

        // 查看隐患
        view(row) {
            // 实现查看逻辑
            this.$message.info(`查看隐患：${row.description}`)
        },

        // 删除隐患
        del(row) {
            elConfirm(`确定删除隐患记录 "${row.description}" 吗？`)
                .then(() => {
                    // 模拟删除操作
                    return this.del.request()
                })
                .then(() => {
                    elSuccess('删除成功')
                    this.refresh()
                })
                .catch(() => {})
        },

        // 开始整改
        startRectification(row) {
            this.currentStatus = 'pending'
            this.statusDialogTitle = '开始整改'
            this.statusForm.id = row.id
            this.statusForm.startTime = new Date().toISOString().replace('T', ' ').substr(0, 19)
            this.statusDialogVisible = true
        },

        // 完成整改
        completeRectification(row) {
            this.currentStatus = 'processing'
            this.statusDialogTitle = '完成整改'
            this.statusForm.id = row.id
            this.statusForm.completeTime = new Date().toISOString().replace('T', ' ').substr(0, 19)
            this.statusDialogVisible = true
        },

        // 验收
        acceptRectification(row) {
            this.currentStatus = 'completed'
            this.statusDialogTitle = '隐患验收'
            this.statusForm.id = row.id
            this.statusForm.acceptTime = new Date().toISOString().replace('T', ' ').substr(0, 19)
            this.statusDialogVisible = true
        },

        // 照片变更处理
        handlePhotoChange(file, fileList) {
            this.photos.push(file.raw)
            this.photoList = fileList
        },

        // 状态照片变更处理
        handleStatusPhotoChange(file, fileList) {
            this.statusPhotos.push(file.raw)
            this.statusPhotoList = fileList
        },

        // 提交表单
        submit() {
            this.$refs.form.validate(valid => {
                if (!valid) return
                
                this.submitting = true
                const request = this.dialogType === 'add' ? this.add.request : this.update.request
                
                // 模拟提交操作
                request()
                    .then(() => {
                        elSuccess(this.dialogType === 'add' ? '新增成功' : '更新成功')
                        this.dialogVisible = false
                        this.refresh()
                    })
                    .finally(() => {
                        this.submitting = false
                    })
            })
        },

        // 提交状态更新
        submitStatusUpdate() {
            this.$refs.statusForm.validate(valid => {
                if (!valid) return
                
                this.submitting = true
                
                // 根据当前状态设置新的状态
                let newStatus = ''
                switch (this.currentStatus) {
                    case 'pending':
                        newStatus = 'processing'
                        break
                    case 'processing':
                        newStatus = 'completed'
                        break
                    case 'completed':
                        newStatus = 'accepted'
                        break
                }
                
                // 模拟提交操作
                this.updateStatus.request()
                    .then(() => {
                        elSuccess('状态更新成功')
                        this.statusDialogVisible = false
                        this.refresh()
                    })
                    .finally(() => {
                        this.submitting = false
                    })
            })
        },

        // 关闭弹窗
        closeDialog() {
            this.$refs.form.resetFields()
            this.form = {
                id: null,
                description: '',
                type: '',
                level: '',
                department: '',
                foundBy: '',
                foundTime: '',
                deadline: '',
                responsiblePerson: '',
                responsibleDept: '',
                rectificationMeasure: '',
                status: 'pending'
            }
            this.photoList = []
            this.photos = []
        },

        // 关闭状态弹窗
        closeStatusDialog() {
            this.$refs.statusForm.resetFields()
            this.statusForm = {
                id: null,
                startTime: '',
                completeTime: '',
                acceptTime: '',
                acceptBy: '',
                description: ''
            }
            this.statusPhotoList = []
            this.statusPhotos = []
        }
    }
}
</script>

<style scoped>
.pagination {
    margin-top: 15px;
    text-align: right;
}
</style> 