<template>
    <div class="app-container doc-page-container">
        <el-card shadow="hover" style="height: 100%">
            <div slot="header">
                <el-form ref="searchForm" :inline="true" :model="search" size="mini">
                    <el-form-item label="保险种类">
                        <el-input v-model.trim="search.insuranceType" placeholder="请输入保险种类" clearable/>
                    </el-form-item>
                    <el-form-item label="保险公司">
                        <el-input v-model.trim="search.company" placeholder="请输入保险公司" clearable/>
                    </el-form-item>
                    <el-form-item label="负责人姓名">
                        <el-input v-model.trim="search.managerName" placeholder="请输入负责人姓名" clearable/>
                    </el-form-item>
                    <el-form-item label="检查状态">
                        <el-select v-model="search.checkStatus" placeholder="请选择检查状态" clearable>
                            <el-option label="正常" value="正常"/>
                            <el-option label="即将过期" value="即将过期"/>
                            <el-option label="已过期" value="已过期"/>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button-group>
                            <el-button type="primary" @click="search.page = 1;refresh(true)">查询</el-button>
                            <el-button type="primary" @click="add">添加</el-button>
                            <el-button type="primary" @click="exportData">导出</el-button>
                        </el-button-group>
                    </el-form-item>
                </el-form>
            </div>

            <el-table
                v-loading="loading"
                :data="list"
                border
                size="mini"
                height="calc(100% - 70px)"
                style="width: 100%"
                @row-dblclick="edit"
            >
                <el-table-column align="center" type="index" width="50"/>
                <el-table-column align="center" prop="insuranceType" label="保险种类" min-width="120"/>
                <el-table-column align="center" prop="company" label="保险公司" min-width="120"/>
                <el-table-column align="center" prop="amount" label="保险金额" width="100">
                    <template slot-scope="{row}">
                        {{row.amount || '未设置'}} {{row.amount ? '元' : ''}}
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="insuredCount" label="参保人数" width="80"/>
                <el-table-column align="center" prop="startDate" label="保险日期" width="100"/>
                <el-table-column align="center" prop="endDate" label="到期日期" width="100"/>
                <el-table-column align="center" label="剩余天数" prop="remainingDays" width="90">
                    <template slot-scope="{row}">
                        <span :class="getRemainingDaysClass(row.remainingDays)">
                            {{row.remainingDays != null ? row.remainingDays + '天' : '未设置'}}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="checkStatus" label="检查状态" width="90">
                    <template slot-scope="{row}">
                        <el-tag :type="getCheckStatusType(row.checkStatus)">
                            {{row.checkStatus || '未知'}}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="managerName" label="负责人" width="80"/>
                <el-table-column align="center" prop="managerPhone" label="负责人电话" width="120"/>
                <el-table-column align="center" prop="alertStatus" label="预警状态" width="90">
                    <template slot-scope="{row}">
                        <el-tag :type="getAlertStatusType(row.alertStatus)">
                            {{row.alertStatus || '未设置'}}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="alertCount" label="预警次数" width="80"/>
                <el-table-column align="center" label="操作" width="180" fixed="right">
                    <template slot-scope="{row}">
                        <el-button
                            type="primary"
                            size="mini"
                            @click="edit(row)"
                        >编辑</el-button>
                        <el-button
                            type="danger"
                            size="mini"
                            @click="handleDelete(row)"
                        >删除</el-button>
                        <el-dropdown size="mini" split-button type="primary" trigger="click" @command="command => handleCommand(command, row)">
                            更多
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item command="setAlert" :disabled="row.alertStatus === '待预警'">设置预警</el-dropdown-item>
                                <el-dropdown-item command="closeAlert" :disabled="row.alertStatus === '已关闭'">关闭预警</el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </template>
                </el-table-column>
            </el-table>

            <el-pagination
                :current-page.sync="page.current"
                :page-sizes="[10, 20, 50, 100]"
                :page-size.sync="page.size"
                :total="total"
                background
                layout="total, sizes, prev, pager, next, jumper"
                @current-change="refresh"
                @size-change="refresh"
                style="margin-top: 10px; text-align: right"
            />

            <!-- 添加/编辑企业保险弹窗 -->
            <el-dialog
                :title="dialogType === 'add' ? '新增企业保险' : '编辑企业保险'"
                :visible.sync="dialogVisible"
                width="600px"
                @closed="closeDialog"
            >
                <el-form ref="form" :model="form" :rules="rules" label-width="120px" size="mini">
                    <el-form-item label="保险种类" prop="insuranceType">
                        <el-input v-model.trim="form.insuranceType" placeholder="请输入保险种类"/>
                    </el-form-item>
                    <el-form-item label="保险公司" prop="company">
                        <el-input v-model.trim="form.company" placeholder="请输入保险公司"/>
                    </el-form-item>
                    <el-form-item label="保险金额" prop="amount">
                        <el-input-number v-model="form.amount" :min="0" :precision="2" :step="1000" style="width: 100%"/>
                    </el-form-item>
                    <el-form-item label="参保人数" prop="insuredCount">
                        <el-input-number v-model="form.insuredCount" :min="0" :precision="0" :step="1" style="width: 100%"/>
                    </el-form-item>
                    <el-form-item label="保险日期" prop="startDate">
                        <el-date-picker
                            v-model="form.startDate"
                            type="date"
                            placeholder="选择保险日期"
                            value-format="yyyy-MM-dd"
                            style="width: 100%"
                        />
                    </el-form-item>
                    <el-form-item label="到期日期" prop="endDate">
                        <el-date-picker
                            v-model="form.endDate"
                            type="date"
                            placeholder="选择到期日期"
                            value-format="yyyy-MM-dd"
                            style="width: 100%"
                        />
                    </el-form-item>
                    <el-form-item label="负责人姓名" prop="managerName">
                        <el-input v-model.trim="form.managerName" placeholder="请输入负责人姓名"/>
                    </el-form-item>
                    <el-form-item label="负责人电话" prop="managerPhone">
                        <el-input v-model.trim="form.managerPhone" placeholder="请输入负责人电话"/>
                    </el-form-item>
                </el-form>
                <div slot="footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" :loading="submitting" @click="submitForm">确定</el-button>
                </div>
            </el-dialog>
        </el-card>
    </div>
</template>

<script>
import {wic} from "@/util/auth"
import {elError, elSuccess, elConfirm} from "@/util/message"
import { search, add, update, del, exportInsurance, setAlert, closeAlert, updateAlertStatus } from "@/api/doc/enterpriseInsurance"
import { timeFormat } from "@/util"

export default {
    name: "enterpriseInsuranceManagement",

    data() {
        return {
            loading: false,
            submitting: false,
            
            search: {
                insuranceType: '',
                company: '',
                managerName: '',
                checkStatus: ''
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
                insuranceType: '',
                company: '',
                amount: null,
                insuredCount: null,
                startDate: '',
                endDate: '',
                managerName: '',
                managerPhone: ''
            },
            
            rules: {
                insuranceType: [
                    { required: true, message: '请输入保险种类', trigger: 'blur' }
                ],
                company: [
                    { required: true, message: '请输入保险公司', trigger: 'blur' }
                ],
                startDate: [
                    { required: true, message: '请选择保险日期', trigger: 'change' }
                ],
                endDate: [
                    { required: true, message: '请选择到期日期', trigger: 'change' }
                ]
            }
        }
    },

    computed: {
        // 权限控制，默认都设为true
        canAdd: () => true,
        canUpdate: () => true,
        canDelete: () => true,
        
        searchParams() {
            const params = {
                ...this.search,
                page: this.page.current,
                pageSize: this.page.size
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
                    console.error('获取企业保险列表失败', err)
                    elError('获取数据失败，请稍后重试')
                })
                .finally(() => {
                    this.loading = false
                })
        },

        // 获取检查状态样式
        getCheckStatusType(status) {
            const types = {
                '正常': 'success',
                '即将过期': 'warning',
                '已过期': 'danger',
                '未设置到期日期': 'info'
            }
            return types[status] || 'info'
        },
        
        // 获取预警状态样式
        getAlertStatusType(status) {
            const types = {
                '未设置': 'info',
                '待预警': 'warning',
                '已提醒': 'success',
                '已关闭': 'danger'
            }
            return types[status] || 'info'
        },
        
        // 获取剩余天数样式
        getRemainingDaysClass(days) {
            if (days == null) return ''
            if (days < 0) return 'text-danger'
            if (days <= 30) return 'text-warning'
            return 'text-success'
        },

        // 添加企业保险
        add() {
            this.dialogType = 'add'
            this.dialogVisible = true
            this.form = {
                id: null,
                insuranceType: '',
                company: '',
                amount: null,
                insuredCount: null,
                startDate: '',
                endDate: '',
                managerName: '',
                managerPhone: ''
            }
        },

        // 编辑企业保险
        edit(row) {
            this.dialogType = 'edit'
            this.dialogVisible = true
            this.form = { ...row }
        },

        // 关闭弹窗
        closeDialog() {
            this.$refs.form && this.$refs.form.resetFields()
        },
        
        // 取消表单
        cancelForm() {
            this.dialogVisible = false
        },

        // 提交表单
        submitForm() {
            this.$refs.form.validate(valid => {
                if (!valid) return
                
                this.submitting = true
                const request = this.dialogType === 'add' ? add : update
                
                request(this.form)
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
                        console.error(this.dialogType === 'add' ? '添加企业保险失败' : '更新企业保险失败', err)
                        elError(this.dialogType === 'add' ? '添加失败，请稍后重试' : '更新失败，请稍后重试')
                    })
                    .finally(() => {
                        this.submitting = false
                    })
            })
        },

        // 删除企业保险
        handleDelete(row) {
            elConfirm(`确定删除企业保险 "${row.insuranceType}" 吗？`)
                .then(() => {
                    return del(row.id)
                })
                .then(res => {
                    if (res.success) {
                        elSuccess('删除成功')
                        this.refresh()
                    } else {
                        elError(res.msg || '删除失败')
                    }
                })
                .catch(err => {
                    console.error('删除企业保险失败', err)
                    elError('删除失败，请稍后重试')
                })
        },

        // 处理命令
        handleCommand(command, row) {
            if (command === 'setAlert') {
                this.handleSetAlert(row)
            } else if (command === 'closeAlert') {
                this.handleCloseAlert(row)
            }
        },
        
        // 设置预警
        handleSetAlert(row) {
            elConfirm(`确定为 "${row.insuranceType}" 设置预警吗？`)
                .then(() => {
                    return setAlert(row.id)
                })
                .then(res => {
                    if (res.success) {
                        elSuccess('预警设置成功')
                        this.refresh()
                    } else {
                        elError(res.msg || '预警设置失败')
                    }
                })
                .catch(err => {
                    console.error('设置预警失败', err)
                    elError('设置预警失败，请稍后重试')
                })
        },
        
        // 关闭预警
        handleCloseAlert(row) {
            elConfirm(`确定关闭 "${row.insuranceType}" 的预警吗？`)
                .then(() => {
                    return closeAlert(row.id)
                })
                .then(res => {
                    if (res.success) {
                        elSuccess('预警关闭成功')
                        this.refresh()
                    } else {
                        elError(res.msg || '预警关闭失败')
                    }
                })
                .catch(err => {
                    console.error('关闭预警失败', err)
                    elError('关闭预警失败，请稍后重试')
                })
        },
        
        // 导出数据
        exportData() {
            exportInsurance(this.searchParams)
                .then(res => {
                    const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
                    const fileName = `企业保险列表_${timeFormat(new Date(), '{y}{m}{d}{h}{i}{s}')}.xlsx`
                    
                    if (window.navigator && window.navigator.msSaveOrOpenBlob) {
                        // IE
                        window.navigator.msSaveOrOpenBlob(blob, fileName)
                    } else {
                        const link = document.createElement('a')
                        link.href = window.URL.createObjectURL(blob)
                        link.download = fileName
                        link.click()
                        URL.revokeObjectURL(link.href)
                    }
                })
                .catch(err => {
                    console.error('导出企业保险列表失败', err)
                    elError('导出失败，请稍后重试')
                })
        }
    }
}
</script>

<style scoped>
.app-container {
    padding: 20px;
}
.pagination {
    margin-top: 20px;
    text-align: right;
}
.text-danger {
    color: #F56C6C;
}
.text-warning {
    color: #E6A23C;
}
.text-success {
    color: #67C23A;
}
</style> 