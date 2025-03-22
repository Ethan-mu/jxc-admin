<template>
    <div class="app-container">
        <el-card>
            <div slot="header">
                <span>公司资质证书管理</span>
            </div>

            <el-form :inline="true" size="mini" @submit.native.prevent>
                <el-form-item>
                    <el-input v-model.trim="search.certificateName" placeholder="证书名称" clearable/>
                </el-form-item>
                <el-form-item>
                    <el-input v-model.trim="search.issuingAuthority" placeholder="颁证单位" clearable/>
                </el-form-item>
                <el-form-item>
                    <el-input v-model.trim="search.managerName" placeholder="管理人员" clearable/>
                </el-form-item>
                <el-form-item>
                    <el-select v-model="search.alertStatus" placeholder="预警状态" clearable>
                        <el-option label="未设置" value="未设置"/>
                        <el-option label="待预警" value="待预警"/>
                        <el-option label="已提醒" value="已提醒"/>
                        <el-option label="已关闭" value="已关闭"/>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" @click="refresh(true)">查询</el-button>
                    <el-button v-if="canAdd" type="success" icon="el-icon-plus" @click="add">新增</el-button>
                    <el-button type="info" icon="el-icon-download" @click="exportData">导出</el-button>
                </el-form-item>
            </el-form>

            <el-table v-loading="loading" :data="data" border>
                <el-table-column label="证书编号" prop="id" width="80" align="center"/>
                <el-table-column label="证书名称" prop="certificateName" min-width="150"/>
                <el-table-column label="颁证单位" prop="issuingAuthority" min-width="150"/>
                <el-table-column label="颁发日期" prop="issueDate" width="120" align="center"/>
                <el-table-column label="换证时间" prop="renewalDate" width="120" align="center"/>
                <el-table-column label="复证时间" prop="reviewDate" width="120" align="center"/>
                <el-table-column label="剩余天数" width="100" align="center">
                    <template slot-scope="{row}">
                        <span :class="getRemainingDaysClass(row.remainingDays)">{{getRemainingDaysText(row.remainingDays)}}</span>
                    </template>
                </el-table-column>
                <el-table-column label="存放位置" prop="storageLocation" min-width="100"/>
                <el-table-column label="管理人员" prop="managerName" width="100"/>
                <el-table-column label="管理人员联系方式" prop="managerContact" min-width="120"/>
                <el-table-column label="主管领导" prop="leaderName" width="100"/>
                <el-table-column label="主管领导联系方式" prop="leaderContact" min-width="120"/>
                <el-table-column label="预警状态" prop="alertStatus" width="100" align="center">
                    <template slot-scope="{row}">
                        <el-tag :type="getAlertStatusType(row.alertStatus)">{{row.alertStatus}}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="280" align="center" fixed="right">
                    <template slot-scope="{row}">
                        <el-button v-if="canView" type="text" size="mini" @click="view(row)">查看</el-button>
                        <el-button v-if="canUpdate" type="text" size="mini" @click="edit(row)">编辑</el-button>
                        <el-button v-if="canDelete" type="text" size="mini" @click="del(row)">删除</el-button>
                        <el-dropdown size="mini" split-button type="text" @command="handleAlertCommand($event, row)">
                            预警操作
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item command="set">设置预警</el-dropdown-item>
                                <el-dropdown-item command="remind">标记已提醒</el-dropdown-item>
                                <el-dropdown-item command="close">关闭预警</el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
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
            :title="dialogType === 'add' ? '新增公司资质证书' : '编辑公司资质证书'"
            :visible.sync="dialogVisible"
            width="600px"
            @closed="closeDialog"
        >
            <el-form ref="form" :model="form" :rules="rules" label-width="120px">
                <el-form-item label="证书名称" prop="certificateName">
                    <el-input v-model.trim="form.certificateName" placeholder="请输入证书名称"/>
                </el-form-item>
                <el-form-item label="颁证单位" prop="issuingAuthority">
                    <el-input v-model.trim="form.issuingAuthority" placeholder="请输入颁证单位"/>
                </el-form-item>
                <el-form-item label="颁发日期" prop="issueDate">
                    <el-date-picker
                        v-model="form.issueDate"
                        type="date"
                        placeholder="选择颁发日期"
                        value-format="yyyy-MM-dd"
                        style="width: 100%"
                    />
                </el-form-item>
                <el-form-item label="换证时间" prop="renewalDate">
                    <el-date-picker
                        v-model="form.renewalDate"
                        type="date"
                        placeholder="选择换证时间"
                        value-format="yyyy-MM-dd"
                        style="width: 100%"
                    />
                </el-form-item>
                <el-form-item label="复证时间" prop="reviewDate">
                    <el-date-picker
                        v-model="form.reviewDate"
                        type="date"
                        placeholder="选择复证时间"
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
            </el-form>
            <div slot="footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" :loading="submitting" @click="submit">确定</el-button>
            </div>
        </el-dialog>

        <!-- 查看证书详情弹窗 -->
        <el-dialog
            title="证书详情"
            :visible.sync="viewDialogVisible"
            width="600px"
        >
            <el-descriptions :column="2" border>
                <el-descriptions-item label="证书名称">{{viewForm.certificateName}}</el-descriptions-item>
                <el-descriptions-item label="颁证单位">{{viewForm.issuingAuthority}}</el-descriptions-item>
                <el-descriptions-item label="颁发日期">{{viewForm.issueDate}}</el-descriptions-item>
                <el-descriptions-item label="换证时间">{{viewForm.renewalDate}}</el-descriptions-item>
                <el-descriptions-item label="复证时间">{{viewForm.reviewDate}}</el-descriptions-item>
                <el-descriptions-item label="存放位置">{{viewForm.storageLocation}}</el-descriptions-item>
                <el-descriptions-item label="管理人员">{{viewForm.managerName}}</el-descriptions-item>
                <el-descriptions-item label="管理人员联系方式">{{viewForm.managerContact}}</el-descriptions-item>
                <el-descriptions-item label="主管领导">{{viewForm.leaderName}}</el-descriptions-item>
                <el-descriptions-item label="主管领导联系方式">{{viewForm.leaderContact}}</el-descriptions-item>
                <el-descriptions-item label="预警状态">
                    <el-tag :type="getAlertStatusType(viewForm.alertStatus)">{{viewForm.alertStatus}}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="剩余天数">
                    <span :class="getRemainingDaysClass(viewForm.remainingDays)">{{getRemainingDaysText(viewForm.remainingDays)}}</span>
                </el-descriptions-item>
            </el-descriptions>
            <div slot="footer">
                <el-button @click="viewDialogVisible = false">关闭</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {wic} from "@/util/auth"
import {elError, elSuccess, elConfirm} from "@/util/message"
import request from '@/api/request'  // 替换为项目中配置好的axios实例

export default {
    name: "companyQualification",

    data() {
        return {
            loading: false,
            submitting: false,
            
            search: {
                certificateName: '',
                issuingAuthority: '',
                managerName: '',
                alertStatus: ''
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
                issuingAuthority: '',
                issueDate: null,
                renewalDate: null,
                reviewDate: null,
                storageLocation: '',
                managerName: '',
                managerContact: '',
                leaderName: '',
                leaderContact: '',
                alertStatus: '未设置',
                alertCount: 0,
                createTime: null,
                updateTime: null
            },
            
            viewDialogVisible: false,
            viewForm: {},
            
            rules: {
                certificateName: [
                    { required: true, message: '请输入证书名称', trigger: 'blur' }
                ],
                issuingAuthority: [
                    { required: true, message: '请输入颁证单位', trigger: 'blur' }
                ],
                issueDate: [
                    { required: true, message: '请选择颁发日期', trigger: 'change' }
                ],
                managerName: [
                    { required: true, message: '请输入管理人员姓名', trigger: 'blur' }
                ],
                managerContact: [
                    { required: false, message: '请输入管理人员联系方式', trigger: 'blur' }
                ],
                storageLocation: [
                    { required: false, message: '请输入存放位置', trigger: 'blur' }
                ]
            }
        }
    },

    computed: {
        // 权限控制
        ...wic({
            add: {url: 'admin_document_qualification_add'},
            update: {url: 'admin_document_qualification_update'},
            delete: {url: 'admin_document_qualification_delete'}
        }),
        canView() { return true }
    },

    created() {
        this.refresh()
    },

    methods: {
        // 处理API响应数据，统一返回格式
        processResponseData(response) {
            if (!response) return { success: false, data: null, list: [], total: 0, msg: '响应为空' }
            
            const success = response.status === 200
            const msg = response.msg || (success ? '操作成功' : '操作失败')
            
            if (!success || !response.data) {
                return { success, data: null, list: [], total: 0, msg }
            }
            
            // 处理data中的数据
            const data = response.data
            console.log('处理响应数据，data:', data)
            
            // 兼容多种格式：
            // 1. MyBatis-Plus默认格式: data.records + data.total
            // 2. 系统标准格式: data.list + data.total
            // 3. 列表直接包含在data中的格式
            let list = [], total = 0
            let formatDetected = false
            
            if (Array.isArray(data.list)) {
                // 系统标准格式
                console.log('检测到系统标准格式 data.list')
                list = data.list
                total = data.total || 0
                formatDetected = true
            } else if (Array.isArray(data.records)) {
                // MyBatis-Plus默认格式
                console.log('检测到MyBatis-Plus默认格式 data.records')
                list = data.records
                
                // 特殊处理: 如果total为0但records有数据，使用records长度作为total
                if (data.total === 0 && list.length > 0) {
                    console.log('检测到total为0但records有数据，使用records长度作为total')
                    total = list.length
                } else {
                    total = data.total || 0
                }
                formatDetected = true
            } else if (Array.isArray(data)) {
                // 直接返回数组的格式
                console.log('检测到数组格式')
                list = data
                total = list.length
                formatDetected = true
            } else if (data.data && Array.isArray(data.data.list)) {
                // 嵌套格式 {data: {data: {list: [], total: 0}}}
                console.log('检测到嵌套格式 data.data.list')
                list = data.data.list
                total = data.data.total || 0
                formatDetected = true
            }
            
            // 如果没有检测到任何已知格式，记录错误
            if (!formatDetected) {
                console.error('无法识别的数据格式:', data)
                // 尝试其他可能的格式
                if (data.data && Array.isArray(data.data.records)) {
                    console.log('尝试读取 data.data.records')
                    list = data.data.records
                    total = data.data.total || list.length
                }
            }
            
            // 确保total不会小于list长度
            if (total < list.length) {
                console.log(`修正total: ${total} -> ${list.length}`)
                total = list.length
            }
            
            console.log(`提取的list长度: ${list.length}, total: ${total}`)
            return { success, data, list, total, msg }
        },

        // 刷新数据
        refresh(reset = false) {
            if (reset) this.page.current = 1
            this.loading = true
            
            // 构建查询参数
            const params = {
                page: this.page.current,
                pageSize: this.page.size,
                ...this.search
            }
            
            console.log('查询参数:', JSON.stringify(params))
            // 调用后端接口
            request.post('/admin/document/qualification/search', params)
                .then(response => {
                    console.log('完整响应对象:', response)
                    
                    // 使用统一的响应处理方法
                    const result = this.processResponseData(response)
                    console.log('处理后的数据:', result)
                    
                    if (result.success) {
                        this.data = result.list
                        this.total = result.total
                        
                        if (this.data.length === 0 && this.total === 0) {
                            console.warn('查询成功但无数据')
                        }
                    } else {
                        elError(result.msg)
                        this.data = []
                        this.total = 0
                    }
                })
                .catch(error => {
                    console.error('获取数据失败', error)
                    elError('获取数据失败')
                    this.data = []
                    this.total = 0
                })
                .finally(() => {
                    this.loading = false
                })
        },
        
        // 获取预警状态类型
        getAlertStatusType(status) {
            const types = {
                '未设置': 'info',
                '待预警': 'warning',
                '已提醒': 'success',
                '已关闭': 'danger'
            }
            return types[status] || 'info'
        },

        // 获取剩余天数文本
        getRemainingDaysText(days) {
            if (days === null || days === undefined) return '未知'
            if (days < 0) return '已过期'
            return `${days}天`
        },
        
        // 获取剩余天数样式
        getRemainingDaysClass(days) {
            if (days === null || days === undefined) return ''
            if (days < 0) return 'text-danger'
            if (days <= 30) return 'text-warning'
            return 'text-success'
        },

        // 新增证书
        add() {
            this.dialogType = 'add'
            this.dialogVisible = true
        },

        // 编辑证书
        edit(row) {
            this.dialogType = 'edit'
            // 创建一个完整的拷贝，确保包含所有字段
            this.form = { 
                ...row,
                // 确保时间字段存在
                createTime: row.createTime || null,
                updateTime: null // 更新时会重新设置
            }
            console.log('编辑证书，表单数据:', this.form)
            this.dialogVisible = true
        },
        
        // 查看证书
        view(row) {
            this.viewForm = { ...row }
            this.viewDialogVisible = true
        },

        // 删除证书
        del(row) {
            elConfirm(`确定删除证书 "${row.certificateName}" 吗？`)
                .then(() => {
                    return request.delete(`/admin/document/qualification/${row.id}`)
                })
                .then(response => {
                    console.log('删除响应:', response)
                    const result = this.processResponseData(response)
                    
                    if (result.success) {
                        elSuccess(result.msg)
                        this.refresh()
                    } else {
                        elError(result.msg)
                    }
                })
                .catch(error => {
                    console.error('删除失败', error)
                    elError('删除失败')
                })
        },
        
        // 导出数据
        exportData() {
            // 构建查询参数
            const params = {
                ...this.search
            }
            
            // 显示加载提示
            this.$message.info('正在准备导出数据，请稍候...')
            
            // 使用axios直接下载文件
            request({
                url: '/admin/document/qualification/export',
                method: 'post',
                data: params,
                responseType: 'blob'
            })
            .then(response => {
                // 创建blob对象
                const blob = new Blob([response.data])
                
                // 创建下载链接
                const downloadElement = document.createElement('a')
                const href = window.URL.createObjectURL(blob) // 创建下载的链接
                
                // 处理文件名
                let filename = '公司资质证书.xlsx'
                const disposition = response.headers['content-disposition']
                
                if (disposition) {
                    // 尝试从Content-Disposition中提取文件名
                    try {
                        // 先尝试提取UTF-8编码的文件名
                        const utf8FilenameRegex = /filename\*=UTF-8''([^;]*)/i
                        const matches = utf8FilenameRegex.exec(disposition)
                        
                        if (matches && matches[1]) {
                            // URL解码
                            filename = decodeURIComponent(matches[1])
                        } else {
                            // 尝试提取普通文件名
                            const filenameRegex = /filename=["']?([^"';]*)/i
                            const plainMatches = filenameRegex.exec(disposition)
                            
                            if (plainMatches && plainMatches[1]) {
                                filename = plainMatches[1]
                                
                                // 尝试处理可能的URL编码
                                try {
                                    filename = decodeURIComponent(filename)
                                } catch (e) {
                                    console.warn('文件名解码失败', e)
                                }
                            }
                        }
                    } catch (e) {
                        console.error('提取文件名失败:', e)
                    }
                }
                
                console.log('提取的文件名:', filename)
                
                downloadElement.href = href
                downloadElement.download = filename
                document.body.appendChild(downloadElement)
                downloadElement.click() // 点击下载
                document.body.removeChild(downloadElement) // 下载完成移除元素
                window.URL.revokeObjectURL(href) // 释放blob对象
                
                this.$message.success('文件下载成功')
            })
            .catch(error => {
                console.error('导出失败:', error)
                this.$message.error('导出失败，请稍后重试')
            })
        },
        
        // 处理预警操作
        handleAlertCommand(command, row) {
            switch (command) {
                case 'set':
                    this.setAlert(row.id)
                    break
                case 'remind':
                    this.updateAlertStatus(row.id, '已提醒')
                    break
                case 'close':
                    this.closeAlert(row.id)
                    break
            }
        },
        
        // 设置预警
        setAlert(id) {
            request.put(`/admin/document/qualification/alert/set/${id}`)
                .then(response => {
                    console.log('设置预警响应:', response)
                    const result = this.processResponseData(response)
                    
                    if (result.success) {
                        elSuccess(result.msg)
                        this.refresh()
                    } else {
                        elError(result.msg)
                    }
                })
                .catch(error => {
                    console.error('设置预警失败', error)
                    elError('设置预警失败')
                })
        },
        
        // 关闭预警
        closeAlert(id) {
            request.put(`/admin/document/qualification/alert/close/${id}`)
                .then(response => {
                    console.log('关闭预警响应:', response)
                    const result = this.processResponseData(response)
                    
                    if (result.success) {
                        elSuccess(result.msg)
                        this.refresh()
                    } else {
                        elError(result.msg)
                    }
                })
                .catch(error => {
                    console.error('关闭预警失败', error)
                    elError('关闭预警失败')
                })
        },

        // 更新预警状态
        updateAlertStatus(id, status) {
            const data = { id, alertStatus: status }
            console.log('更新预警状态请求数据:', data)
            
            request.put(`/admin/document/qualification/alert/status`, data)
                .then(response => {
                    console.log('更新预警状态响应:', response)
                    const result = this.processResponseData(response)
                    
                    if (result.success) {
                        elSuccess(result.msg)
                        this.refresh()
                    } else {
                        elError(result.msg)
                    }
                })
                .catch(error => {
                    console.error('更新预警状态失败', error)
                    elError('更新预警状态失败')
                })
        },

        // 关闭对话框
        closeDialog() {
            this.form = {
                id: null,
                certificateName: '',
                issuingAuthority: '',
                issueDate: null,
                renewalDate: null,
                reviewDate: null,
                storageLocation: '',
                managerName: '',
                managerContact: '',
                leaderName: '',
                leaderContact: '',
                alertStatus: '未设置',
                alertCount: 0,
                createTime: null,
                updateTime: null
            }
        },

        // 提交表单
        submit() {
            this.$refs.form.validate((valid) => {
                if (valid) {
                    this.submitting = true
                    
                    // 创建一个深拷贝，避免直接修改表单
                    const formData = JSON.parse(JSON.stringify(this.form))
                    
                    // 可能需要进行一些数据清理/转换
                    // 例如：确保alertCount是数字类型
                    if (formData.alertCount !== null && formData.alertCount !== undefined) {
                        formData.alertCount = parseInt(formData.alertCount)
                    }
                    
                    // 根据对话框类型决定使用哪个API
                    const url = this.dialogType === 'add' 
                        ? '/admin/document/qualification' 
                        : '/admin/document/qualification'
                    
                    const method = this.dialogType === 'add' ? 'post' : 'put'
                    
                    // 设置时间戳
                    const now = Date.now(); // 使用毫秒级时间戳
                    if (this.dialogType === 'add') {
                        // 新增证书时设置创建时间
                        formData.createTime = now
                    } else {
                        // 更新证书时设置更新时间
                        formData.updateTime = now
                        // 保留原有的创建时间
                        if (!formData.createTime && this.form.createTime) {
                            formData.createTime = this.form.createTime
                        }
                    }
                    
                    console.log('准备发送请求:', method.toUpperCase(), url)
                    console.log('提交数据:', JSON.stringify(formData))
                    
                    request({
                        method: method,
                        url: url,
                        data: formData
                    })
                        .then(response => {
                            console.log('提交响应:', response)
                            this.submitting = false
                            
                            const result = this.processResponseData(response)
                            
                            if (result.success) {
                                elSuccess(result.msg)
                                this.dialogVisible = false
                                this.refresh()
                            } else {
                                elError(result.msg)
                            }
                        })
                        .catch(error => {
                            console.error('操作失败', error)
                            elError('操作失败: ' + (error.message || JSON.stringify(error)))
                            this.submitting = false
                        })
                }
            })
        },
        
        // 添加证书方法
        addCertificate(formData) {
            return request.post('/admin/document/qualification', formData)
        },
        
        // 更新证书方法
        updateCertificate(formData) {
            return request.put(`/admin/document/qualification`, formData)
        },
        
        // 删除证书方法
        deleteCertificate(id) {
            return request.delete(`/admin/document/qualification/${id}`)
        }
    }
}
</script>

<style scoped>
.text-danger {
    color: #F56C6C;
    font-weight: bold;
}
.text-warning {
    color: #E6A23C;
    font-weight: bold;
}
.text-success {
    color: #67C23A;
    font-weight: bold;
}
.pagination {
    margin-top: 15px;
    text-align: right;
}
</style>