<template>
    <div class="app-container">
        <el-card>
            <div slot="header">
                <span>报告管理</span>
            </div>

            <el-form :inline="true" size="mini" @submit.native.prevent>
                <el-form-item>
                    <el-input v-model.trim="search.certificateName" placeholder="报告名称" clearable/>
                </el-form-item>
                <el-form-item>
                    <el-select v-model="search.reportType" placeholder="报告类型" clearable>
                        <el-option label="安全报告" value="safety"/>
                        <el-option label="质量报告" value="quality"/>
                        <el-option label="环保报告" value="environment"/>
                        <el-option label="年度检查报告" value="annual"/>
                        <el-option label="月度检查报告" value="monthly"/>
                        <el-option label="其他报告" value="other"/>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-date-picker
                        v-model="search.dateRange"
                        type="daterange"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        value-format="yyyy-MM-dd"
                        clearable
                    />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" @click="refresh(true)">查询</el-button>
                    <el-button v-if="canAdd" type="success" icon="el-icon-plus" @click="add">新增</el-button>
                    <el-button type="warning" icon="el-icon-download" @click="exportData">导出</el-button>
                </el-form-item>
            </el-form>

            <el-table
                ref="table"
                v-loading="loading"
                :data="list"
                border
                stripe
                size="mini"
                @row-dblclick="handleEdit"
            >
                <el-table-column type="index" width="50"/>
                <el-table-column prop="certificateName" label="报告名称" min-width="150"/>
                
                <el-table-column label="报告类型" min-width="120">
                    <template slot-scope="{row}">
                        {{ getTypeText(row.reportType) }}
                    </template>
                </el-table-column>
                
                <el-table-column prop="compilingUnit" label="编制单位" min-width="120"/>
                
                <el-table-column prop="compileDate" label="编制日期" min-width="120"/>
                
                <el-table-column prop="recordDate" label="备案日期" min-width="120"/>
                
                <el-table-column prop="nextRecordDate" label="下次备案日期" min-width="120"/>
                
                <el-table-column prop="managerName" label="管理人员" min-width="100"/>
                
                <el-table-column label="预警状态" width="100">
                    <template slot-scope="{row}">
                        <el-tag :type="getAlertStatusType(row.alertStatus)">
                            {{ row.alertStatus === 1 ? '已预警' : '正常' }}
                        </el-tag>
                    </template>
                </el-table-column>
                
                <el-table-column label="剩余天数" width="100">
                    <template slot-scope="{row}">
                        <span :class="getRemainingDaysClass(row.remainingDays)">
                            {{ row.remainingDays }}
                        </span>
                    </template>
                </el-table-column>
                
                <el-table-column label="操作" width="180" fixed="right">
                    <template slot-scope="{row}">
                        <el-button
                            type="success"
                            size="mini"
                            title="查看详情"
                            @click="handleView(row)"
                        >
                            <i class="el-icon-view"/>
                        </el-button>
                        <el-button
                            type="primary"
                            size="mini"
                            title="修改"
                            @click="handleEdit(row)"
                        >
                            <i class="el-icon-edit"/>
                        </el-button>
                        <el-button
                            type="danger"
                            size="mini"
                            title="删除"
                            @click="handleDelete(row)"
                        >
                            <i class="el-icon-delete"/>
                        </el-button>
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

        <!-- 添加/编辑报告弹窗 -->
        <el-dialog
            :title="dialogType === 'add' ? '新增报告' : '编辑报告'"
            :visible.sync="dialogVisible"
            width="600px"
            @closed="closeDialog"
        >
            <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="报告名称" prop="certificateName">
                    <el-input v-model.trim="form.certificateName" placeholder="请输入报告名称"/>
                </el-form-item>
                <el-form-item label="报告类型" prop="reportType">
                    <el-select v-model="form.reportType" placeholder="请选择报告类型" style="width: 100%">
                        <el-option label="安全报告" value="safety"/>
                        <el-option label="质量报告" value="quality"/>
                        <el-option label="环保报告" value="environment"/>
                        <el-option label="年度检查报告" value="annual"/>
                        <el-option label="月度检查报告" value="monthly"/>
                        <el-option label="其他报告" value="other"/>
                    </el-select>
                </el-form-item>
                <el-form-item label="编制单位" prop="compilingUnit">
                    <el-input v-model.trim="form.compilingUnit" placeholder="请输入编制单位"/>
                </el-form-item>
                <el-form-item label="编制日期" prop="compileDate">
                    <el-date-picker
                        v-model="form.compileDate"
                        type="date"
                        placeholder="选择编制日期"
                        value-format="yyyy-MM-dd"
                        style="width: 100%"
                    />
                </el-form-item>
                <el-form-item label="备案/备查日期" prop="recordDate">
                    <el-date-picker
                        v-model="form.recordDate"
                        type="date"
                        placeholder="选择备案/备查日期"
                        value-format="yyyy-MM-dd"
                        style="width: 100%"
                    />
                </el-form-item>
                <el-form-item label="下次备案日期" prop="nextRecordDate">
                    <el-date-picker
                        v-model="form.nextRecordDate"
                        type="date"
                        placeholder="选择下次备案日期"
                        value-format="yyyy-MM-dd"
                        style="width: 100%"
                    />
                </el-form-item>
                <el-form-item label="存放位置" prop="storageLocation">
                    <el-input v-model.trim="form.storageLocation" placeholder="请输入存放位置"/>
                </el-form-item>
                <el-form-item label="管理人员姓名" prop="managerName">
                    <el-input v-model.trim="form.managerName" placeholder="请输入管理人员姓名"/>
                </el-form-item>
                <el-form-item label="管理人员联系方式" prop="managerContact">
                    <el-input v-model.trim="form.managerContact" placeholder="请输入管理人员联系方式"/>
                </el-form-item>
                <el-form-item label="主管领导姓名" prop="leaderName">
                    <el-input v-model.trim="form.leaderName" placeholder="请输入主管领导姓名"/>
                </el-form-item>
                <el-form-item label="主管领导联系方式" prop="leaderContact">
                    <el-input v-model.trim="form.leaderContact" placeholder="请输入主管领导联系方式"/>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm">提交</el-button>
                    <el-button @click="cancelForm">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
import {wic} from "@/util/auth"
import {elError, elSuccess, elConfirm} from "@/util/message"
import { search, add, update, del, exportReport, setAlert, closeAlert, updateAlertStatus } from "@/api/doc/report"
import { parseTime } from "@/util"

export default {
    name: "reportManagement",

    data() {
        return {
            loading: false,
            submitting: false,
            
            search: {
                certificateName: '',
                reportType: '',
                dateRange: []
            },
            
            list: [], // 表格数据
            total: 0,
            page: {
                current: 1,
                size: 10
            },
            
            dialogType: 'add', // 'add' 或 'edit'
            dialogVisible: false,
            form: {
                id: null,
                certificateName: '',
                reportType: '',
                compilingUnit: '',
                compileDate: '',
                recordDate: '',
                nextRecordDate: '',
                storageLocation: '',
                managerName: '',
                managerContact: '',
                leaderName: '',
                leaderContact: ''
            },
            
            rules: {
                certificateName: [
                    { required: true, message: '请输入报告名称', trigger: 'blur' }
                ],
                reportType: [
                    { required: true, message: '请选择报告类型', trigger: 'change' }
                ],
                compileDate: [
                    { required: true, message: '请选择编制日期', trigger: 'change' }
                ],
                recordDate: [
                    { required: true, message: '请选择备案/备查日期', trigger: 'change' }
                ],
                nextRecordDate: [
                    { required: true, message: '请选择下次备案日期', trigger: 'change' }
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
        canDownload() { return true },
        // 构建实际的搜索参数
        searchParams() {
            const params = {
                certificateName: this.search.certificateName,
                reportType: this.search.reportType,
                page: this.page.current,
                pageSize: this.page.size
            }
            
            if (this.search.dateRange && this.search.dateRange.length === 2) {
                params.compileDateBegin = this.search.dateRange[0]
                params.compileDateEnd = this.search.dateRange[1]
            }
            
            return params
        }
    },

    created() {
        this.refresh()
    },

    methods: {
        // 刷新数据
        refresh(reset = false) {
            if (reset) this.page.current = 1
            this.loading = true
            
            search(this.searchParams)
                .then(res => {
                    if (res.success) {
                        this.list = res.data.records || []
                        this.total = res.data.total || 0
                    } else {
                        elError(res.msg || '获取数据失败')
                    }
                })
                .catch(err => {
                    console.error('获取报告列表失败', err)
                    elError('获取数据失败，请稍后重试')
                })
                .finally(() => {
                    this.loading = false
                })
        },
        
        // 获取报告类型文本
        getTypeText(type) {
            const typeMap = {
                'safety': '安全报告',
                'quality': '质量报告',
                'environment': '环保报告',
                'annual': '年度检查报告',
                'monthly': '月度检查报告',
                'other': '其他报告'
            }
            return typeMap[type] || type
        },
        
        // 打开添加报告弹窗
        add() {
            this.dialogType = 'add'
            this.dialogVisible = true
            this.form = {
                id: null,
                certificateName: '',
                reportType: '',
                compilingUnit: '',
                compileDate: '',
                recordDate: '',
                nextRecordDate: '',
                storageLocation: '',
                managerName: '',
                managerContact: '',
                leaderName: '',
                leaderContact: ''
            }
        },
        
        // 编辑报告
        handleEdit(row) {
            this.dialogVisible = true
            this.dialogTitle = '编辑报告'
            this.editMode = true
            this.form = {
                id: row.id,
                certificateName: row.certificateName,
                reportType: row.reportType,
                compilingUnit: row.compilingUnit,
                compileDate: row.compileDate,
                recordDate: row.recordDate,
                nextRecordDate: row.nextRecordDate,
                storageLocation: row.storageLocation,
                managerName: row.managerName,
                managerContact: row.managerContact,
                leaderName: row.leaderName,
                leaderContact: row.leaderContact
            }
        },
        
        // 关闭弹窗
        closeDialog() {
            this.$refs.form && this.$refs.form.resetFields()
        },
        
        // 提交表单
        submitForm() {
            this.$refs.form.validate(valid => {
                if (!valid) return
                
                this.submitting = true
                
                const request = this.dialogType === 'add' ? add(this.form) : update(this.form)
                
                request
                    .then(res => {
                        if (res.success) {
                            elSuccess(this.dialogType === 'add' ? '添加成功' : '更新成功')
                            this.dialogVisible = false
                            this.refresh()
                        } else {
                            elError(res.msg || (this.dialogType === 'add' ? '添加失败' : '更新失败'))
                        }
                    })
                    .catch(err => {
                        console.error(this.dialogType === 'add' ? '添加报告失败' : '更新报告失败', err)
                        elError(this.dialogType === 'add' ? '添加失败，请稍后重试' : '更新失败，请稍后重试')
                    })
                    .finally(() => {
                        this.submitting = false
                    })
            })
        },
        
        // 取消表单
        cancelForm() {
            this.dialogVisible = false
        },
        
        // 查看报告
        handleView(row) {
            this.$alert(`
                <div>
                    <h3>${row.certificateName}</h3>
                    <p><strong>报告类型：</strong>${this.getTypeText(row.reportType)}</p>
                    <p><strong>编制单位：</strong>${row.compilingUnit}</p>
                    <p><strong>编制日期：</strong>${row.compileDate}</p>
                    <p><strong>备案日期：</strong>${row.recordDate}</p>
                    <p><strong>下次备案日期：</strong>${row.nextRecordDate}</p>
                    <p><strong>存放位置：</strong>${row.storageLocation || '无'}</p>
                    <p><strong>管理人员：</strong>${row.managerName} (${row.managerContact})</p>
                    <p><strong>主管领导：</strong>${row.leaderName} (${row.leaderContact})</p>
                </div>
            `, '报告详情', {
                dangerouslyUseHTMLString: true
            })
        },
        
        // 删除报告
        handleDelete(row) {
            this.$confirm('确认删除该报告?', '提示', {
                type: 'warning'
            }).then(() => {
                this.loading = true
                del(row.id)
                    .then(res => {
                        if (res.success) {
                            this.$message.success('删除成功')
                            this.fetchData()
                        } else {
                            this.$message.error(res.msg || '删除失败')
                        }
                    })
                    .finally(() => {
                        this.loading = false
                    })
            }).catch(() => {})
        },
        
        // 下载报告
        download(row) {
            if (row.filePath) {
                window.open(row.filePath)
            } else {
                this.$message.warning('该报告没有可下载的文件')
            }
        },
        
        // 导出报告
        exportData() {
            exportReport(this.searchParams)
                .then(res => {
                    const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
                    const link = document.createElement('a')
                    link.href = URL.createObjectURL(blob)
                    link.download = '报告列表.xlsx'
                    link.click()
                    URL.revokeObjectURL(link.href)
                })
                .catch(err => {
                    console.error('导出报告失败', err)
                    elError('导出失败，请稍后重试')
                })
        },
        
        // 获取预警状态类型
        getAlertStatusType(status) {
            return status === 1 ? 'danger' : 'success'
        },
        
        // 获取剩余天数类名
        getRemainingDaysClass(days) {
            if (days <= 0) return 'text-danger'
            if (days <= 30) return 'text-warning'
            return 'text-success'
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