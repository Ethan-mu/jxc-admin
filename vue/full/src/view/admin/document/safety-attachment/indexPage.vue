<template>
    <div class="app-container doc-page-container">
        <el-card shadow="hover" style="height: 100%">
            <div slot="header">
                <el-form :inline="true" size="mini" @submit.native.prevent>
                    <el-form-item>
                        <el-input v-model.trim="search.attachType" placeholder="附件类型" clearable/>
                    </el-form-item>
                    <el-form-item>
                        <el-input v-model.trim="search.unit" placeholder="单位" clearable/>
                    </el-form-item>
                    <el-form-item>
                        <el-select v-model="search.status" placeholder="使用状态" clearable>
                            <el-option label="正常" value="正常"/>
                            <el-option label="损坏" value="损坏"/>
                            <el-option label="待更换" value="待更换"/>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select v-model="search.checkStatus" placeholder="检查状态" clearable>
                            <el-option label="正常" value="正常"/>
                            <el-option label="即将过期" value="即将过期"/>
                            <el-option label="已过期" value="已过期"/>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button-group>
                            <el-button type="primary" icon="el-icon-search" @click="refresh(true)">查询</el-button>
                            <el-button v-if="canAdd" type="primary" icon="el-icon-plus" @click="add">新增</el-button>
                            <el-button v-if="canExport" type="primary" icon="el-icon-download" @click="exportData">导出</el-button>
                        </el-button-group>
                    </el-form-item>
                </el-form>
            </div>

            <el-table v-loading="loading" :data="data" border size="mini" height="calc(100% - 70px)" style="width: 100%">
                <el-table-column label="ID" prop="id" width="80" align="center"/>
                <el-table-column label="附件类型" prop="attachType" min-width="120"/>
                <el-table-column label="单位" prop="unit" width="80" align="center"/>
                <el-table-column label="数量" prop="quantity" width="80" align="center"/>
                <el-table-column label="使用状态" prop="status" width="100" align="center">
                    <template slot-scope="{row}">
                        <el-tag :type="getStatusType(row.status)">{{row.status}}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="管理人员" prop="managerName" min-width="100"/>
                <el-table-column label="管理人员联系方式" prop="managerContact" min-width="120"/>
                <el-table-column label="主管领导" prop="leaderName" min-width="100"/>
                <el-table-column label="储存位置" prop="storageLocation" min-width="120"/>
                <el-table-column label="使用日期" prop="useDate" width="100" align="center"/>
                <el-table-column label="复检日期" prop="recheckDate" width="100" align="center"/>
                <el-table-column label="剩余天数" prop="remainingDays" width="90" align="center">
                    <template slot-scope="{row}">
                        <span :class="{'text-danger': row.remainingDays < 30}">{{row.remainingDays}}天</span>
                    </template>
                </el-table-column>
                <el-table-column label="检查状态" prop="checkStatus" width="100" align="center">
                    <template slot-scope="{row}">
                        <el-tag :type="getCheckStatusType(row.checkStatus)">{{row.checkStatus}}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="预警状态" prop="alertStatus" width="100" align="center">
                    <template slot-scope="{row}">
                        <el-tag :type="getAlertStatusType(row.alertStatus)">{{row.alertStatus}}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="240" align="center" fixed="right">
                    <template slot-scope="{row}">
                        <el-button v-if="canUpdate" type="primary" size="mini" @click="edit(row)">编辑</el-button>
                        <el-button v-if="canDelete" type="danger" size="mini" @click="del(row)">删除</el-button>
                        <el-dropdown size="mini" split-button type="primary" @command="command => handleCommand(command, row)">
                            更多
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item command="setAlert" :disabled="row.alertStatus !== '未设置' && row.alertStatus !== '已关闭'">设置预警</el-dropdown-item>
                                <el-dropdown-item command="closeAlert" :disabled="row.alertStatus === '未设置' || row.alertStatus === '已关闭'">关闭预警</el-dropdown-item>
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
                @size-change="refresh"
                @current-change="refresh"
                style="margin-top: 15px; text-align: right"
            />
        </el-card>

        <!-- 添加/编辑安全附件弹窗 -->
        <el-dialog
            :title="dialogType === 'add' ? '新增安全附件' : '编辑安全附件'"
            :visible.sync="dialogVisible"
            width="650px"
            @closed="closeDialog"
        >
            <el-form ref="form" :model="form" :rules="rules" label-width="120px">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="附件类型" prop="attachType">
                            <el-input v-model.trim="form.attachType" placeholder="请输入附件类型"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="单位" prop="unit">
                            <el-input v-model.trim="form.unit" placeholder="请输入单位"/>
                        </el-form-item>
                    </el-col>
                </el-row>
                
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="数量" prop="quantity">
                            <el-input-number v-model="form.quantity" :min="1" :max="9999" style="width: 100%"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="使用状态" prop="status">
                            <el-select v-model="form.status" placeholder="请选择使用状态" style="width: 100%">
                                <el-option label="正常" value="正常"/>
                                <el-option label="损坏" value="损坏"/>
                                <el-option label="待更换" value="待更换"/>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="管理人员姓名" prop="managerName">
                            <el-input v-model.trim="form.managerName" placeholder="请输入管理人员姓名"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="管理人员联系方式" prop="managerContact">
                            <el-input v-model.trim="form.managerContact" placeholder="请输入管理人员联系方式"/>
                        </el-form-item>
                    </el-col>
                </el-row>
                
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="主管领导姓名" prop="leaderName">
                            <el-input v-model.trim="form.leaderName" placeholder="请输入主管领导姓名"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="主管领导联系方式" prop="leaderContact">
                            <el-input v-model.trim="form.leaderContact" placeholder="请输入主管领导联系方式"/>
                        </el-form-item>
                    </el-col>
                </el-row>
                
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="储存位置" prop="storageLocation">
                            <el-input v-model.trim="form.storageLocation" placeholder="请输入储存位置"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="使用日期" prop="useDate">
                            <el-date-picker
                                v-model="form.useDate"
                                type="date"
                                placeholder="选择使用日期"
                                value-format="yyyy-MM-dd"
                                style="width: 100%"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
                
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="复检日期" prop="recheckDate">
                            <el-date-picker
                                v-model="form.recheckDate"
                                type="date"
                                placeholder="选择复检日期"
                                value-format="yyyy-MM-dd"
                                style="width: 100%"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
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
import {search, add, update, del, setAlert, closeAlert, updateAlertStatus, exportAttachment} from "@/api/doc/safetyAttachment"
import {mapState} from 'vuex'

export default {
    name: "safetyAttachment",

    data() {
        return {
            loading: false,
            submitting: false,
            
            search: {
                attachType: '',
                unit: '',
                status: '',
                checkStatus: '',
                page: 1,
                pageSize: 10
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
                uid: null,
                attachType: '',
                unit: '',
                quantity: 1,
                status: '正常',
                managerName: '',
                managerContact: '',
                leaderName: '',
                leaderContact: '',
                storageLocation: '',
                useDate: '',
                recheckDate: '',
                alertStatus: '未设置',
                alertCount: 0
            },
            
            rules: {
                attachType: [
                    { required: true, message: '请输入附件类型', trigger: 'blur' }
                ],
                unit: [
                    { required: true, message: '请输入单位', trigger: 'blur' }
                ],
                quantity: [
                    { required: true, message: '请输入数量', trigger: 'change' }
                ],
                status: [
                    { required: true, message: '请选择使用状态', trigger: 'change' }
                ],
                recheckDate: [
                    { required: true, message: '请选择复检日期', trigger: 'change' }
                ]
            }
        }
    },

    computed: {
        // 权限控制
        ...wic({
            add: {request: add},
            update: {request: update},
            del: {request: del}
        }),
        canExport() { return true },
        ...mapState({
            userInfo: state => state.user.info
        })
    },

    created() {
        this.refresh()
    },

    methods: {
        // 刷新数据
        refresh(reset = false) {
            if (reset) this.page.current = 1
            this.loading = true
            
            const params = {
                attachType: this.search.attachType,
                unit: this.search.unit,
                status: this.search.status,
                checkStatus: this.search.checkStatus,
                page: this.page.current,
                pageSize: this.page.size
            }
            
            search(params)
                .then(res => {
                    const {data} = res
                    if (data && data.records) {
                        this.data = data.records
                        this.total = data.total
                    } else {
                        this.data = []
                        this.total = 0
                    }
                })
                .catch(error => {
                    if(error) elError(error)
                    this.data = []
                    this.total = 0
                })
                .finally(() => {
                    this.loading = false
                })
        },

        // 获取使用状态类型
        getStatusType(status) {
            const types = {
                '正常': 'success',
                '损坏': 'danger',
                '待更换': 'warning'
            }
            return types[status] || 'info'
        },

        // 获取检查状态类型
        getCheckStatusType(status) {
            const types = {
                '正常': 'success',
                '即将过期': 'warning',
                '已过期': 'danger',
                '未设置复检日期': 'info'
            }
            return types[status] || 'info'
        },

        // 获取预警状态类型
        getAlertStatusType(status) {
            const types = {
                '未设置': 'info',
                '待预警': 'warning',
                '已提醒': 'danger',
                '已关闭': 'success'
            }
            return types[status] || 'info'
        },

        // 新增安全附件
        add() {
            this.dialogType = 'add'
            this.dialogVisible = true
        },

        // 编辑安全附件
        edit(row) {
            this.dialogType = 'edit'
            this.form = JSON.parse(JSON.stringify(row))
            this.dialogVisible = true
        },

        // 删除安全附件
        del(row) {
            elConfirm(`确定删除 "${row.attachType}" 吗？`)
                .then(() => {
                    return del(row.id)
                })
                .then(() => {
                    elSuccess('删除成功')
                    this.refresh()
                })
                .catch(error => {
                    if(error) elError(error)
                })
        },

        // 设置预警
        setAlert(row) {
            elConfirm(`确定对 "${row.attachType}" 设置预警吗？`)
                .then(() => {
                    return setAlert(row.id)
                })
                .then(() => {
                    elSuccess('预警设置成功')
                    this.refresh()
                })
                .catch(error => {
                    if(error) elError(error)
                })
        },

        // 关闭预警
        closeAlert(row) {
            elConfirm(`确定关闭 "${row.attachType}" 的预警吗？`)
                .then(() => {
                    return closeAlert(row.id)
                })
                .then(() => {
                    elSuccess('预警已关闭')
                    this.refresh()
                })
                .catch(error => {
                    if(error) elError(error)
                })
        },

        // 导出数据
        exportData() {
            const params = {
                ...this.search
            }
            
            exportAttachment(params)
                .then(res => {
                    const url = window.URL.createObjectURL(new Blob([res]))
                    const link = document.createElement('a')
                    link.href = url
                    link.setAttribute('download', '安全附件列表.xlsx')
                    document.body.appendChild(link)
                    link.click()
                    document.body.removeChild(link)
                })
                .catch(error => {
                    if(error) elError(error)
                })
        },

        // 提交表单
        submit() {
            this.$refs.form.validate(valid => {
                if (!valid) return
                
                this.submitting = true
                
                // 设置用户ID
                if (this.dialogType === 'add' && this.userInfo && this.userInfo.id) {
                    this.form.uid = this.userInfo.id;
                }
                
                const request = this.dialogType === 'add' ? add : update
                
                request(this.form)
                    .then(() => {
                        elSuccess(this.dialogType === 'add' ? '新增成功' : '更新成功')
                        this.dialogVisible = false
                        this.refresh()
                    })
                    .catch(error => {
                        if(error) elError(error)
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
                uid: null,
                attachType: '',
                unit: '',
                quantity: 1,
                status: '正常',
                managerName: '',
                managerContact: '',
                leaderName: '',
                leaderContact: '',
                storageLocation: '',
                useDate: '',
                recheckDate: '',
                alertStatus: '未设置',
                alertCount: 0
            }
        },

        // 处理命令
        handleCommand(command, row) {
            if (command === 'setAlert') {
                this.setAlert(row)
            } else if (command === 'closeAlert') {
                this.closeAlert(row)
            }
        }
    }
}
</script>

<style scoped>
.pagination {
    margin-top: 15px;
    text-align: right;
}
.text-danger {
    color: #F56C6C;
    font-weight: bold;
}
</style> 