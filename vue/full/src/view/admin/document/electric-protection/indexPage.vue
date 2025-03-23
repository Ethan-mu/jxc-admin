<template>
    <div class="app-container">
        <el-card>
            <div slot="header">
                <span>电动防护工器具管理</span>
            </div>

            <el-form :inline="true" size="mini" @submit.native.prevent>
                <el-form-item>
                    <el-input v-model.trim="search.name" placeholder="工器具名称" clearable/>
                </el-form-item>
                <el-form-item>
                    <el-select v-model="search.status" placeholder="检测状态" clearable>
                        <el-option label="合格" value="qualified"/>
                        <el-option label="待检测" value="pending"/>
                        <el-option label="不合格" value="unqualified"/>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" @click="refresh(true)">查询</el-button>
                    <el-button v-if="canAdd" type="success" icon="el-icon-plus" @click="add">新增</el-button>
                </el-form-item>
            </el-form>

            <el-table v-loading="loading" :data="data" border>
                <el-table-column label="编号" prop="id" width="100" align="center"/>
                <el-table-column label="工器具名称" prop="name" min-width="150"/>
                <el-table-column label="型号规格" prop="model" min-width="100"/>
                <el-table-column label="绝缘等级" prop="insulationLevel" width="100" align="center"/>
                <el-table-column label="数量" prop="quantity" width="80" align="center"/>
                <el-table-column label="购买日期" prop="purchaseDate" width="120" align="center"/>
                <el-table-column label="上次检测日期" prop="lastTestDate" width="120" align="center"/>
                <el-table-column label="下次检测日期" prop="nextTestDate" width="120" align="center"/>
                <el-table-column label="检测状态" prop="status" width="100" align="center">
                    <template slot-scope="{row}">
                        <el-tag :type="getStatusType(row.status)">{{getStatusText(row.status)}}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="存放位置" prop="location" min-width="120"/>
                <el-table-column label="操作" width="200" align="center" fixed="right">
                    <template slot-scope="{row}">
                        <el-button v-if="canView" type="text" size="mini" @click="view(row)">查看</el-button>
                        <el-button v-if="canUpdate" type="text" size="mini" @click="edit(row)">编辑</el-button>
                        <el-button v-if="canDelete" type="text" size="mini" @click="del(row)">删除</el-button>
                        <el-button v-if="canTest" type="text" size="mini" @click="test(row)">检测</el-button>
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

        <!-- 添加/编辑工器具弹窗 -->
        <el-dialog
            :title="dialogType === 'add' ? '新增工器具' : '编辑工器具'"
            :visible.sync="dialogVisible"
            width="500px"
            @closed="closeDialog"
        >
            <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="工器具名称" prop="name">
                    <el-input v-model.trim="form.name" placeholder="请输入工器具名称"/>
                </el-form-item>
                <el-form-item label="型号规格" prop="model">
                    <el-input v-model.trim="form.model" placeholder="请输入型号规格"/>
                </el-form-item>
                <el-form-item label="绝缘等级" prop="insulationLevel">
                    <el-select v-model="form.insulationLevel" placeholder="请选择绝缘等级">
                        <el-option label="A级" value="A"/>
                        <el-option label="B级" value="B"/>
                        <el-option label="C级" value="C"/>
                        <el-option label="D级" value="D"/>
                    </el-select>
                </el-form-item>
                <el-form-item label="数量" prop="quantity">
                    <el-input-number v-model="form.quantity" :min="1" :max="999" placeholder="请输入数量"/>
                </el-form-item>
                <el-form-item label="购买日期" prop="purchaseDate">
                    <el-date-picker
                        v-model="form.purchaseDate"
                        type="date"
                        placeholder="选择购买日期"
                        value-format="yyyy-MM-dd"
                    />
                </el-form-item>
                <el-form-item label="上次检测日期" prop="lastTestDate">
                    <el-date-picker
                        v-model="form.lastTestDate"
                        type="date"
                        placeholder="选择上次检测日期"
                        value-format="yyyy-MM-dd"
                    />
                </el-form-item>
                <el-form-item label="下次检测日期" prop="nextTestDate">
                    <el-date-picker
                        v-model="form.nextTestDate"
                        type="date"
                        placeholder="选择下次检测日期"
                        value-format="yyyy-MM-dd"
                    />
                </el-form-item>
                <el-form-item label="存放位置" prop="location">
                    <el-input v-model.trim="form.location" placeholder="请输入存放位置"/>
                </el-form-item>
                <el-form-item label="照片" prop="file">
                    <el-upload
                        class="upload-demo"
                        action="#"
                        :auto-upload="false"
                        :on-change="handleFileChange"
                        :limit="1"
                        :file-list="fileList"
                    >
                        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                        <div slot="tip" class="el-upload__tip">支持jpg、png、jpeg格式图片</div>
                    </el-upload>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model.trim="form.remark" type="textarea" :rows="3" placeholder="请输入备注信息"/>
                </el-form-item>
            </el-form>
            <div slot="footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" :loading="submitting" @click="submit">确定</el-button>
            </div>
        </el-dialog>

        <!-- 检测工器具弹窗 -->
        <el-dialog
            title="工器具检测"
            :visible.sync="testDialogVisible"
            width="500px"
            @closed="closeTestDialog"
        >
            <el-form ref="testForm" :model="testForm" :rules="testRules" label-width="100px">
                <el-form-item label="检测日期" prop="testDate">
                    <el-date-picker
                        v-model="testForm.testDate"
                        type="date"
                        placeholder="选择检测日期"
                        value-format="yyyy-MM-dd"
                    />
                </el-form-item>
                <el-form-item label="检测结果" prop="result">
                    <el-select v-model="testForm.result" placeholder="请选择检测结果">
                        <el-option label="合格" value="qualified"/>
                        <el-option label="不合格" value="unqualified"/>
                    </el-select>
                </el-form-item>
                <el-form-item label="下次检测日期" prop="nextTestDate">
                    <el-date-picker
                        v-model="testForm.nextTestDate"
                        type="date"
                        placeholder="选择下次检测日期"
                        value-format="yyyy-MM-dd"
                    />
                </el-form-item>
                <el-form-item label="检测机构" prop="testOrg">
                    <el-input v-model.trim="testForm.testOrg" placeholder="请输入检测机构"/>
                </el-form-item>
                <el-form-item label="检测报告" prop="reportFile">
                    <el-upload
                        class="upload-demo"
                        action="#"
                        :auto-upload="false"
                        :on-change="handleReportFileChange"
                        :limit="1"
                        :file-list="reportFileList"
                    >
                        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                        <div slot="tip" class="el-upload__tip">可上传PDF、Word、Excel等格式文件</div>
                    </el-upload>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model.trim="testForm.remark" type="textarea" :rows="3" placeholder="请输入备注信息"/>
                </el-form-item>
            </el-form>
            <div slot="footer">
                <el-button @click="testDialogVisible = false">取消</el-button>
                <el-button type="primary" :loading="submitting" @click="submitTest">确定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {wic} from "@/util/auth"
import {elError, elSuccess, elConfirm} from "@/util/message"

export default {
    name: "electricProtection",

    data() {
        return {
            loading: false,
            submitting: false,
            
            search: {
                name: '',
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
                name: '',
                model: '',
                insulationLevel: '',
                quantity: 1,
                purchaseDate: '',
                lastTestDate: '',
                nextTestDate: '',
                location: '',
                remark: '',
                file: null
            },
            fileList: [],
            
            testDialogVisible: false,
            testForm: {
                id: null,
                testDate: '',
                result: '',
                nextTestDate: '',
                testOrg: '',
                reportFile: null,
                remark: ''
            },
            reportFileList: [],
            
            rules: {
                name: [
                    { required: true, message: '请输入工器具名称', trigger: 'blur' }
                ],
                model: [
                    { required: true, message: '请输入型号规格', trigger: 'blur' }
                ],
                insulationLevel: [
                    { required: true, message: '请选择绝缘等级', trigger: 'change' }
                ],
                quantity: [
                    { required: true, message: '请输入数量', trigger: 'change' }
                ],
                purchaseDate: [
                    { required: true, message: '请选择购买日期', trigger: 'change' }
                ],
                location: [
                    { required: true, message: '请输入存放位置', trigger: 'blur' }
                ]
            },
            
            testRules: {
                testDate: [
                    { required: true, message: '请选择检测日期', trigger: 'change' }
                ],
                result: [
                    { required: true, message: '请选择检测结果', trigger: 'change' }
                ],
                nextTestDate: [
                    { required: true, message: '请选择下次检测日期', trigger: 'change' }
                ],
                testOrg: [
                    { required: true, message: '请输入检测机构', trigger: 'blur' }
                ],
                reportFile: [
                    { required: true, message: '请上传检测报告', trigger: 'change' }
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
            test: {request: () => Promise.resolve({})}
        }),
        canView() { return true },
        canDownload() { return true },
        canTest() { return true }
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
                        name: '绝缘手套',
                        model: 'XJD-500',
                        insulationLevel: 'B',
                        quantity: 10,
                        purchaseDate: '2023-05-10',
                        lastTestDate: '2023-11-15',
                        nextTestDate: '2024-05-15',
                        status: 'qualified',
                        location: '一号仓库A区',
                        remark: '高压作业用绝缘手套'
                    },
                    {
                        id: '002',
                        name: '绝缘靴',
                        model: 'JYX-2000',
                        insulationLevel: 'C',
                        quantity: 5,
                        purchaseDate: '2023-01-20',
                        lastTestDate: '2023-06-20',
                        nextTestDate: '2023-12-20',
                        status: 'pending',
                        location: '二号仓库B区',
                        remark: '高压作业用绝缘靴'
                    }
                ]
                this.total = 2
            }, 500)
        },

        // 获取状态类型
        getStatusType(status) {
            const types = {
                qualified: 'success',
                pending: 'warning',
                unqualified: 'danger'
            }
            return types[status] || 'info'
        },

        // 获取状态文本
        getStatusText(status) {
            const texts = {
                qualified: '合格',
                pending: '待检测',
                unqualified: '不合格'
            }
            return texts[status] || '未知'
        },

        // 新增工器具
        add() {
            this.dialogType = 'add'
            this.dialogVisible = true
        },

        // 编辑工器具
        edit(row) {
            this.dialogType = 'edit'
            this.form = { ...row }
            this.dialogVisible = true
        },

        // 查看工器具
        view(row) {
            // 实现查看逻辑
            this.$message.info(`查看工器具：${row.name}`)
        },

        // 删除工器具
        del(row) {
            elConfirm(`确定删除工器具 "${row.name}" 吗？`)
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

        // 检测工器具
        test(row) {
            this.testForm.id = row.id
            this.testForm.testDate = new Date().toISOString().split('T')[0]
            
            // 计算默认的下次检测日期（6个月后）
            const nextDate = new Date()
            nextDate.setMonth(nextDate.getMonth() + 6)
            this.testForm.nextTestDate = nextDate.toISOString().split('T')[0]
            
            this.testDialogVisible = true
        },

        // 文件变更处理
        handleFileChange(file) {
            this.form.file = file
        },

        // 检测报告文件变更处理
        handleReportFileChange(file) {
            this.testForm.reportFile = file
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

        // 提交检测数据
        submitTest() {
            this.$refs.testForm.validate(valid => {
                if (!valid) return
                
                this.submitting = true
                
                // 模拟提交操作
                this.test.request()
                    .then(() => {
                        elSuccess('检测数据提交成功')
                        this.testDialogVisible = false
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
                name: '',
                model: '',
                insulationLevel: '',
                quantity: 1,
                purchaseDate: '',
                lastTestDate: '',
                nextTestDate: '',
                location: '',
                remark: '',
                file: null
            }
            this.fileList = []
        },

        // 关闭检测弹窗
        closeTestDialog() {
            this.$refs.testForm.resetFields()
            this.testForm = {
                id: null,
                testDate: '',
                result: '',
                nextTestDate: '',
                testOrg: '',
                reportFile: null,
                remark: ''
            }
            this.reportFileList = []
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