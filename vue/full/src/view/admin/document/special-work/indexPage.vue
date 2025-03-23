<template>
    <div class="app-container doc-page-container">
        <el-card shadow="hover" style="height: 100%">
            <div slot="header">
                <el-form :inline="true" size="mini" @submit.native.prevent>
                    <el-form-item>
                        <el-input v-model.trim="search.name" placeholder="证书名称" clearable/>
                    </el-form-item>
                    <el-form-item>
                        <el-select v-model="search.status" placeholder="证书状态" clearable>
                            <el-option label="有效" value="valid"/>
                            <el-option label="即将过期" value="expiring"/>
                            <el-option label="已过期" value="expired"/>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button-group>
                            <el-button type="primary" icon="el-icon-search" @click="refresh(true)">查询</el-button>
                            <el-button v-if="canAdd" type="primary" icon="el-icon-plus" @click="add">新增</el-button>
                        </el-button-group>
                    </el-form-item>
                </el-form>
            </div>

            <el-table v-loading="loading" :data="data" border size="mini" height="calc(100% - 70px)" style="width: 100%">
                <el-table-column label="证书编号" prop="id" width="100" align="center"/>
                <el-table-column label="证书名称" prop="name" min-width="150"/>
                <el-table-column label="发证机构" prop="issuer" min-width="150"/>
                <el-table-column label="发证日期" prop="issueDate" width="120" align="center"/>
                <el-table-column label="有效期至" prop="expireDate" width="120" align="center"/>
                <el-table-column label="状态" prop="status" width="100" align="center">
                    <template slot-scope="{row}">
                        <el-tag :type="getStatusType(row.status)">{{getStatusText(row.status)}}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="200" align="center" fixed="right">
                    <template slot-scope="{row}">
                        <el-button v-if="canView" type="primary" size="mini" @click="view(row)">查看</el-button>
                        <el-button v-if="canUpdate" type="primary" size="mini" @click="edit(row)">编辑</el-button>
                        <el-button v-if="canDelete" type="danger" size="mini" @click="del(row)">删除</el-button>
                        <el-button v-if="canDownload" type="primary" size="mini" @click="download(row)">下载</el-button>
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

        <!-- 添加/编辑证书弹窗 -->
        <el-dialog
            :title="dialogType === 'add' ? '新增证书' : '编辑证书'"
            :visible.sync="dialogVisible"
            width="500px"
            @closed="closeDialog"
        >
            <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="证书名称" prop="name">
                    <el-input v-model.trim="form.name" placeholder="请输入证书名称"/>
                </el-form-item>
                <el-form-item label="发证机构" prop="issuer">
                    <el-input v-model.trim="form.issuer" placeholder="请输入发证机构"/>
                </el-form-item>
                <el-form-item label="发证日期" prop="issueDate">
                    <el-date-picker
                        v-model="form.issueDate"
                        type="date"
                        placeholder="选择发证日期"
                        value-format="yyyy-MM-dd"
                    />
                </el-form-item>
                <el-form-item label="有效期至" prop="expireDate">
                    <el-date-picker
                        v-model="form.expireDate"
                        type="date"
                        placeholder="选择有效期"
                        value-format="yyyy-MM-dd"
                    />
                </el-form-item>
                <el-form-item label="证书文件" prop="file">
                    <el-upload
                        class="upload-demo"
                        action="#"
                        :auto-upload="false"
                        :on-change="handleFileChange"
                        :limit="1"
                        :file-list="fileList"
                    >
                        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                        <div slot="tip" class="el-upload__tip">可上传PDF、Word、Excel等格式文件</div>
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
    </div>
</template>

<script>
import {wic} from "@/util/auth"
import {elError, elSuccess, elConfirm} from "@/util/message"

export default {
    name: "companyQualification",

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
                issuer: '',
                issueDate: '',
                expireDate: '',
                remark: '',
                file: null
            },
            fileList: [],
            
            rules: {
                name: [
                    { required: true, message: '请输入证书名称', trigger: 'blur' }
                ],
                issuer: [
                    { required: true, message: '请输入发证机构', trigger: 'blur' }
                ],
                issueDate: [
                    { required: true, message: '请选择发证日期', trigger: 'change' }
                ],
                expireDate: [
                    { required: true, message: '请选择有效期', trigger: 'change' }
                ],
                file: [
                    { required: true, message: '请上传证书文件', trigger: 'change' }
                ]
            }
        }
    },

    computed: {
        // 权限控制
        ...wic({
            add: {request: () => Promise.resolve({})},
            update: {request: () => Promise.resolve({})},
            del: {request: () => Promise.resolve({})}
        }),
        canView() { return true },
        canDownload() { return true }
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
                        name: '安全生产许可证',
                        issuer: '应急管理局',
                        issueDate: '2023-01-01',
                        expireDate: '2026-01-01',
                        status: 'valid',
                        remark: '公司安全生产许可证'
                    },
                    {
                        id: '002',
                        name: '危险化学品经营许可证',
                        issuer: '应急管理局',
                        issueDate: '2023-06-01',
                        expireDate: '2024-06-01',
                        status: 'expiring',
                        remark: '危险化学品经营许可证'
                    }
                ]
                this.total = 2
            }, 500)
        },

        // 获取状态类型
        getStatusType(status) {
            const types = {
                valid: 'success',
                expiring: 'warning',
                expired: 'danger'
            }
            return types[status] || 'info'
        },

        // 获取状态文本
        getStatusText(status) {
            const texts = {
                valid: '有效',
                expiring: '即将过期',
                expired: '已过期'
            }
            return texts[status] || '未知'
        },

        // 新增证书
        add() {
            this.dialogType = 'add'
            this.dialogVisible = true
        },

        // 编辑证书
        edit(row) {
            this.dialogType = 'edit'
            this.form = { ...row }
            this.dialogVisible = true
        },

        // 查看证书
        view(row) {
            // 实现查看逻辑
            this.$message.info(`查看证书：${row.name}`)
        },

        // 删除证书
        del(row) {
            elConfirm(`确定删除证书 "${row.name}" 吗？`)
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

        // 下载证书
        download(row) {
            // 实现下载逻辑
            this.$message.info(`下载证书：${row.name}`)
        },

        // 文件变更处理
        handleFileChange(file) {
            this.form.file = file
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

        // 关闭弹窗
        closeDialog() {
            this.$refs.form.resetFields()
            this.form = {
                id: null,
                name: '',
                issuer: '',
                issueDate: '',
                expireDate: '',
                remark: '',
                file: null
            }
            this.fileList = []
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