<template>
    <div class="app-container doc-page-container">
        <el-card shadow="hover" style="height: 100%">
            <div slot="header">
                <el-form :inline="true" size="mini" @submit.native.prevent>
                    <el-form-item>
                        <el-input v-model.trim="search.certificateName" placeholder="证书名称" clearable/>
                    </el-form-item>
                    <el-form-item>
                        <el-select v-model="search.status" placeholder="证书状态" clearable>
                            <el-option label="有效" value="0"/>
                            <el-option label="即将过期" value="1"/>
                            <el-option label="已过期" value="2"/>
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
                <el-table-column label="证书名称" prop="certificateName" min-width="120"/>
                <el-table-column label="设备类型" prop="equipmentType" min-width="120"/>
                <el-table-column label="规格型号" prop="specifications" min-width="120"/>
                <el-table-column label="出厂编号" prop="factoryNo" min-width="120"/>
                <el-table-column label="注册编号" prop="registerNo" min-width="120"/>
                <el-table-column label="使用证编号" prop="usageCert" min-width="120"/>
                <el-table-column label="安装位置" prop="installLocation" min-width="120"/>
                <el-table-column label="投用日期" prop="useDate" width="100" align="center"/>
                <el-table-column label="首次检验" prop="firstCheck" width="100" align="center"/>
                <el-table-column label="上次检验" prop="lastCheck" width="100" align="center"/>
                <el-table-column label="下次检验" prop="recheckDate" width="100" align="center"/>
                <el-table-column label="剩余天数" prop="remainingDays" width="90" align="center">
                    <template slot-scope="{row}">
                        <span :class="{'text-danger': row.remainingDays < 30}">{{row.remainingDays}}天</span>
                    </template>
                </el-table-column>
                <el-table-column label="检验状态" prop="checkStatus" width="100" align="center">
                    <template slot-scope="{row}">
                        <el-tag :type="getStatusType(row.checkStatus)">{{getStatusText(row.checkStatus)}}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="预警状态" prop="alertStatus" width="100" align="center">
                    <template slot-scope="{row}">
                        <el-tag :type="row.alertStatus === 0 ? 'info' : 'danger'">
                            {{row.alertStatus === 0 ? '正常' : '预警中'}}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="240" align="center" fixed="right">
                    <template slot-scope="{row}">
                        <el-button v-if="canUpdate" type="primary" size="mini" @click="edit(row)">编辑</el-button>
                        <el-button v-if="canDelete" type="danger" size="mini" @click="del(row)">删除</el-button>
                        <el-button v-if="row.alertStatus === 0" type="primary" size="mini" @click="setAlert(row)">设置预警</el-button>
                        <el-button v-else type="primary" size="mini" @click="closeAlert(row)">关闭预警</el-button>
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

        <!-- 添加/编辑设备弹窗 -->
        <el-dialog
            :title="dialogType === 'add' ? '新增特种设备' : '编辑特种设备'"
            :visible.sync="dialogVisible"
            width="650px"
            @closed="closeDialog"
        >
            <el-form ref="form" :model="form" :rules="rules" label-width="120px">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="证书名称" prop="certificateName">
                            <el-input v-model.trim="form.certificateName" placeholder="请输入证书名称"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="设备类型" prop="equipmentType">
                            <el-input v-model.trim="form.equipmentType" placeholder="请输入设备类型"/>
                        </el-form-item>
                    </el-col>
                </el-row>
                
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="规格型号" prop="specifications">
                            <el-input v-model.trim="form.specifications" placeholder="请输入规格型号"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="出厂编号" prop="factoryNo">
                            <el-input v-model.trim="form.factoryNo" placeholder="请输入出厂编号"/>
                        </el-form-item>
                    </el-col>
                </el-row>
                
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="注册编号" prop="registerNo">
                            <el-input v-model.trim="form.registerNo" placeholder="请输入注册编号"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="使用证编号" prop="usageCert">
                            <el-input v-model.trim="form.usageCert" placeholder="请输入使用证编号"/>
                        </el-form-item>
                    </el-col>
                </el-row>
                
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="管理人姓名" prop="managerName">
                            <el-input v-model.trim="form.managerName" placeholder="请输入管理人姓名"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="管理人联系方式" prop="managerContact">
                            <el-input v-model.trim="form.managerContact" placeholder="请输入管理人联系方式"/>
                        </el-form-item>
                    </el-col>
                </el-row>
                
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="负责人姓名" prop="leaderName">
                            <el-input v-model.trim="form.leaderName" placeholder="请输入负责人姓名"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="负责人联系方式" prop="leaderContact">
                            <el-input v-model.trim="form.leaderContact" placeholder="请输入负责人联系方式"/>
                        </el-form-item>
                    </el-col>
                </el-row>
                
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="生产厂家" prop="manufacturer">
                            <el-input v-model.trim="form.manufacturer" placeholder="请输入生产厂家"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="安装位置" prop="installLocation">
                            <el-input v-model.trim="form.installLocation" placeholder="请输入安装位置"/>
                        </el-form-item>
                    </el-col>
                </el-row>
                
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="投用日期" prop="useDate">
                            <el-date-picker
                                v-model="form.useDate"
                                type="date"
                                placeholder="选择投用日期"
                                value-format="yyyy-MM-dd"
                                style="width: 100%"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="首次检验" prop="firstCheck">
                            <el-date-picker
                                v-model="form.firstCheck"
                                type="date"
                                placeholder="选择首次检验日期"
                                value-format="yyyy-MM-dd"
                                style="width: 100%"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
                
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="上次检验" prop="lastCheck">
                            <el-date-picker
                                v-model="form.lastCheck"
                                type="date"
                                placeholder="选择上次检验日期"
                                value-format="yyyy-MM-dd"
                                style="width: 100%"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="下次检验" prop="recheckDate">
                            <el-date-picker
                                v-model="form.recheckDate"
                                type="date"
                                placeholder="选择下次检验日期"
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
import {search, add, update, del, updateAlert, setAlert, closeAlert, exportEquipment} from "@/api/doc/specialEquipment"

export default {
    name: "specialEquipment",

    data() {
        return {
            loading: false,
            submitting: false,
            
            search: {
                certificateName: '',
                status: '',
                page: 1,
                limit: 10
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
                certificateName: '',
                equipmentType: '',
                specifications: '',
                factoryNo: '',
                registerNo: '',
                usageCert: '',
                managerName: '',
                managerContact: '',
                leaderName: '',
                leaderContact: '',
                manufacturer: '',
                installLocation: '',
                useDate: '',
                firstCheck: '',
                lastCheck: '',
                recheckDate: '',
                status: 0
            },
            
            rules: {
                certificateName: [
                    { required: true, message: '请输入证书名称', trigger: 'blur' }
                ],
                equipmentType: [
                    { required: true, message: '请输入设备类型', trigger: 'blur' }
                ],
                registerNo: [
                    { required: true, message: '请输入注册编号', trigger: 'blur' }
                ],
                recheckDate: [
                    { required: true, message: '请选择下次检验日期', trigger: 'change' }
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
        canExport() { return true }
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
                certificateName: this.search.certificateName,
                status: this.search.status ? parseInt(this.search.status) : '',
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

        // 获取状态类型
        getStatusType(status) {
            const types = {
                0: 'success', // 正常
                1: 'warning', // 即将过期
                2: 'danger'   // 已过期
            }
            return types[status] || 'info'
        },

        // 获取状态文本
        getStatusText(status) {
            const texts = {
                0: '正常',
                1: '即将过期',
                2: '已过期'
            }
            return texts[status] || '未知'
        },

        // 新增设备
        add() {
            this.dialogType = 'add'
            this.dialogVisible = true
        },

        // 编辑设备
        edit(row) {
            this.dialogType = 'edit'
            this.form = JSON.parse(JSON.stringify(row))
            this.dialogVisible = true
        },

        // 删除设备
        del(row) {
            elConfirm(`确定删除 "${row.certificateName}" 吗？`)
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
            elConfirm(`确定对 "${row.certificateName}" 设置预警吗？`)
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
            elConfirm(`确定关闭 "${row.certificateName}" 的预警吗？`)
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
            
            exportEquipment(params)
                .then(res => {
                    const url = window.URL.createObjectURL(new Blob([res]))
                    const link = document.createElement('a')
                    link.href = url
                    link.setAttribute('download', '特种设备列表.xlsx')
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
                certificateName: '',
                equipmentType: '',
                specifications: '',
                factoryNo: '',
                registerNo: '',
                usageCert: '',
                managerName: '',
                managerContact: '',
                leaderName: '',
                leaderContact: '',
                manufacturer: '',
                installLocation: '',
                useDate: '',
                firstCheck: '',
                lastCheck: '',
                recheckDate: '',
                status: 0
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