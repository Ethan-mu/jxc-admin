<template>
    <div class="app-container">
        <el-card>
            <div slot="header">
                <span>档案管理</span>
            </div>

            <el-form :inline="true" size="mini" @submit.native.prevent>
                <el-form-item>
                    <el-input v-model.trim="search.name" placeholder="档案名称" clearable/>
                </el-form-item>
                <el-form-item>
                    <el-input v-model.trim="search.category" placeholder="档案类别" clearable/>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" @click="refresh(true)">查询</el-button>
                    <el-button v-if="canAdd" type="success" icon="el-icon-plus" @click="add">新增</el-button>
                </el-form-item>
            </el-form>

            <el-table v-loading="loading" :data="data" border>
                <el-table-column label="档案编号" prop="id" width="100" align="center"/>
                <el-table-column label="档案名称" prop="name" min-width="150"/>
                <el-table-column label="档案类别" prop="category" min-width="120"/>
                <el-table-column label="上传时间" prop="createTime" min-width="160" align="center"/>
                <el-table-column label="上传人" prop="createBy" min-width="120" align="center"/>
                <el-table-column label="操作" width="200" align="center" fixed="right">
                    <template slot-scope="{row}">
                        <el-button v-if="canView" type="text" size="mini" @click="view(row)">查看</el-button>
                        <el-button v-if="canUpdate" type="text" size="mini" @click="edit(row)">编辑</el-button>
                        <el-button v-if="canDelete" type="text" size="mini" @click="del(row)">删除</el-button>
                        <el-button v-if="canDownload" type="text" size="mini" @click="download(row)">下载</el-button>
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

        <!-- 添加/编辑档案弹窗 -->
        <el-dialog
            :title="dialogType === 'add' ? '新增档案' : '编辑档案'"
            :visible.sync="dialogVisible"
            width="500px"
            @closed="closeDialog"
        >
            <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="档案名称" prop="name">
                    <el-input v-model.trim="form.name" placeholder="请输入档案名称"/>
                </el-form-item>
                <el-form-item label="档案类别" prop="category">
                    <el-input v-model.trim="form.category" placeholder="请输入档案类别"/>
                </el-form-item>
                <el-form-item label="档案文件" prop="file">
                    <el-upload
                        class="upload-demo"
                        action="#"
                        :auto-upload="false"
                        :on-change="handleFileChange"
                        :limit="1"
                        :file-list="fileList"
                    >
                        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                        <div slot="tip" class="el-upload__tip">可上传任意文件类型</div>
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
    name: "documentManagement",

    data() {
        return {
            loading: false,
            submitting: false,
            
            search: {
                name: '',
                category: ''
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
                category: '',
                remark: '',
                file: null
            },
            fileList: [],
            
            rules: {
                name: [
                    { required: true, message: '请输入档案名称', trigger: 'blur' }
                ],
                category: [
                    { required: true, message: '请输入档案类别', trigger: 'blur' }
                ],
                file: [
                    { required: true, message: '请上传档案文件', trigger: 'change' }
                ]
            }
        }
    },

    computed: {
        // 权限控制，这里先模拟，实际应该根据您的权限系统进行配置
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
                        name: '员工手册',
                        category: '规章制度',
                        createTime: '2025-03-16 10:00:00',
                        createBy: '管理员',
                        remark: '公司员工手册'
                    },
                    {
                        id: '002',
                        name: '财务报表模板',
                        category: '财务',
                        createTime: '2025-03-15 14:30:00',
                        createBy: '财务主管',
                        remark: '季度财务报表模板'
                    }
                ]
                this.total = 2
            }, 500)
        },

        // 新增档案
        add() {
            this.dialogType = 'add'
            this.dialogVisible = true
        },

        // 编辑档案
        edit(row) {
            this.dialogType = 'edit'
            this.form = { ...row }
            this.dialogVisible = true
        },

        // 查看档案
        view(row) {
            // 实现查看逻辑
            this.$message.info(`查看档案：${row.name}`)
        },

        // 删除档案
        del(row) {
            elConfirm(`确定删除档案 "${row.name}" 吗？`)
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

        // 下载档案
        download(row) {
            // 实现下载逻辑
            this.$message.info(`下载档案：${row.name}`)
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
                category: '',
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