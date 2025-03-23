<template>
    <div class="app-container">
        <el-card>
            <div slot="header">
                <span>安全管理人员证书管理</span>
            </div>

            <el-form :inline="true" size="mini" @submit.native.prevent>
                <el-form-item>
                    <el-input v-model.trim="search.certificateName" placeholder="证书名称" clearable/>
                </el-form-item>
                <el-form-item>
                    <el-input v-model.trim="search.holderName" placeholder="持证人姓名" clearable/>
                </el-form-item>
                <el-form-item>
                    <el-input v-model.trim="search.idCard" placeholder="身份证号" clearable/>
                </el-form-item>
                <el-form-item>
                    <el-select v-model="search.certType" placeholder="取证类型" clearable>
                        <el-option label="安全管理" value="safety"/>
                        <el-option label="消防管理" value="fire"/>
                        <el-option label="特种作业" value="special"/>
                        <el-option label="其他" value="other"/>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-date-picker
                        v-model="search.dateRange"
                        type="daterange"
                        range-separator="至"
                        start-placeholder="取证日期"
                        end-placeholder="复审日期"
                        value-format="yyyy-MM-dd"
                        clearable
                    />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" @click="refresh(true)">查询</el-button>
                    <el-button type="success" icon="el-icon-plus" @click="add">新增</el-button>
                    <el-button type="warning" icon="el-icon-download" @click="exportData">导出</el-button>
                </el-form-item>
            </el-form>

            <el-table v-loading="loading" :data="data" border>
                <el-table-column type="index" width="50"/>
                <el-table-column label="证书名称" prop="certificateName" min-width="150"/>
                <el-table-column label="持证人姓名" prop="holderName" min-width="120"/>
                <el-table-column label="联系方式" prop="holderPhone" min-width="120"/>
                <el-table-column label="身份证号" prop="idCard" min-width="150"/>
                <el-table-column label="性别" prop="gender" width="60" align="center"/>
                <el-table-column label="取证类型" min-width="120">
                    <template slot-scope="{row}">
                        {{ getCertTypeText(row.certType) }}
                    </template>
                </el-table-column>
                <el-table-column label="岗位" prop="position" min-width="120"/>
                <el-table-column label="发证单位" prop="issuer" min-width="150"/>
                <el-table-column label="证书编码" prop="certCode" min-width="120"/>
                <el-table-column label="取证日期" prop="issueDate" min-width="120"/>
                <el-table-column label="复审日期" prop="reviewDate" min-width="120"/>
                <el-table-column label="预警状态" width="100">
                    <template slot-scope="{row}">
                        <el-tag :type="getAlertStatusType(row.alertStatus)">
                            {{ row.alertStatus || '未设置' }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="剩余天数" width="100">
                    <template slot-scope="{row}">
                        <span :class="getRemainingDaysClass(row.remainingDays)">
                            {{ row.remainingDays !== null ? row.remainingDays : '未设置' }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="200" align="center" fixed="right">
                    <template slot-scope="{row}">
                        <el-button type="text" size="mini" @click="view(row)">查看</el-button>
                        <el-button type="text" size="mini" @click="edit(row)">编辑</el-button>
                        <el-button type="text" size="mini" @click="del(row)">删除</el-button>
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
            width="600px"
            @closed="closeDialog"
        >
            <el-form ref="form" :model="form" :rules="rules" label-width="120px">
                <el-form-item label="证书名称" prop="certificateName">
                    <el-input v-model.trim="form.certificateName" placeholder="请输入证书名称"/>
                </el-form-item>
                <el-form-item label="持证人姓名" prop="holderName">
                    <el-input v-model.trim="form.holderName" placeholder="请输入持证人姓名"/>
                </el-form-item>
                <el-form-item label="联系方式" prop="holderPhone">
                    <el-input v-model.trim="form.holderPhone" placeholder="请输入联系方式"/>
                </el-form-item>
                <el-form-item label="身份证号" prop="idCard">
                    <el-input v-model.trim="form.idCard" placeholder="请输入身份证号"/>
                </el-form-item>
                <el-form-item label="性别" prop="gender">
                    <el-radio-group v-model="form.gender">
                        <el-radio label="男">男</el-radio>
                        <el-radio label="女">女</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="取证类型" prop="certType">
                    <el-select v-model="form.certType" placeholder="请选择取证类型" style="width: 100%">
                        <el-option label="安全管理" value="safety"/>
                        <el-option label="消防管理" value="fire"/>
                        <el-option label="特种作业" value="special"/>
                        <el-option label="其他" value="other"/>
                    </el-select>
                </el-form-item>
                <el-form-item label="岗位" prop="position">
                    <el-input v-model.trim="form.position" placeholder="请输入岗位"/>
                </el-form-item>
                <el-form-item label="入职时间" prop="hireDate">
                    <el-date-picker
                        v-model="form.hireDate"
                        type="date"
                        placeholder="选择入职时间"
                        value-format="yyyy-MM-dd"
                        style="width: 100%"
                    />
                </el-form-item>
                <el-form-item label="发证单位" prop="issuer">
                    <el-input v-model.trim="form.issuer" placeholder="请输入发证单位"/>
                </el-form-item>
                <el-form-item label="主管领导姓名" prop="leaderName">
                    <el-input v-model.trim="form.leaderName" placeholder="请输入主管领导姓名"/>
                </el-form-item>
                <el-form-item label="主管领导电话" prop="leaderContact">
                    <el-input v-model.trim="form.leaderContact" placeholder="请输入主管领导电话"/>
                </el-form-item>
                <el-form-item label="证书编码" prop="certCode">
                    <el-input v-model.trim="form.certCode" placeholder="请输入证书编码"/>
                </el-form-item>
                <el-form-item label="取证日期" prop="issueDate">
                    <el-date-picker
                        v-model="form.issueDate"
                        type="date"
                        placeholder="选择取证日期"
                        value-format="yyyy-MM-dd"
                        style="width: 100%"
                    />
                </el-form-item>
                <el-form-item label="复审日期" prop="reviewDate">
                    <el-date-picker
                        v-model="form.reviewDate"
                        type="date"
                        placeholder="选择复审日期"
                        value-format="yyyy-MM-dd"
                        style="width: 100%"
                    />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" :loading="submitting" @click="submitForm">提交</el-button>
                    <el-button @click="cancelForm">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
import { search, add, update, del, exportCert } from "@/api/doc/safetyWorkerCert"
import { elError, elSuccess, elConfirm } from "@/util/message"

export default {
    name: "safetyWorkerCert",

    data() {
        return {
            loading: false,
            submitting: false,
            
            search: {
                certificateName: '',
                holderName: '',
                idCard: '',
                certType: '',
                dateRange: []
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
                holderName: '',
                holderPhone: '',
                idCard: '',
                gender: '男',
                certType: '',
                position: '',
                hireDate: '',
                issuer: '',
                leaderName: '',
                leaderContact: '',
                certCode: '',
                issueDate: '',
                reviewDate: '',
                alertStatus: '未设置',
                alertCount: 0
            },
            
            rules: {
                certificateName: [
                    { required: true, message: '请输入证书名称', trigger: 'blur' }
                ],
                holderName: [
                    { required: true, message: '请输入持证人姓名', trigger: 'blur' }
                ],
                idCard: [
                    { required: true, message: '请输入身份证号', trigger: 'blur' },
                    { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入正确的身份证号', trigger: 'blur' }
                ],
                gender: [
                    { required: true, message: '请选择性别', trigger: 'change' }
                ],
                certType: [
                    { required: true, message: '请选择取证类型', trigger: 'change' }
                ],
                issueDate: [
                    { required: true, message: '请选择取证日期', trigger: 'change' }
                ]
            }
        }
    },
    
    computed: {
        searchParams() {
            const params = {
                certificateName: this.search.certificateName,
                holderName: this.search.holderName,
                idCard: this.search.idCard,
                certType: this.search.certType,
                page: this.page.current,
                pageSize: this.page.size
            }
            
            if (this.search.dateRange && this.search.dateRange.length === 2) {
                params.issueDateStart = this.search.dateRange[0]
                params.reviewDateEnd = this.search.dateRange[1]
            }
            
            return params
        }
    },
    
    created() {
        this.refresh()
    },
    
    methods: {
        // 处理响应数据，统一处理不同的返回格式
        processResponseData(response) {
            if (!response) return { success: false, data: null, list: [], total: 0, msg: '响应为空' }
            
            const success = response.code === 200 || response.status === 200
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
                    formatDetected = true
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
        
        // 刷新列表数据
        refresh(resetPage = false) {
            if (resetPage) {
                this.page.current = 1
            }
            
            this.loading = true
            search(this.searchParams).then(response => {
                console.log('搜索响应:', response)
                const result = this.processResponseData(response)
                
                if (result.success) {
                    this.data = result.list
                    this.total = result.total
                } else {
                    elError(result.msg || '获取数据失败')
                    this.data = []
                    this.total = 0
                }
            }).catch(error => {
                console.error('搜索失败:', error)
                elError('获取数据失败')
                this.data = []
                this.total = 0
            }).finally(() => {
                this.loading = false
            })
        },
        
        // 导出数据
        exportData() {
            exportCert(this.searchParams)
                .then(response => {
                    // 创建下载链接
                    const blob = new Blob([response], { type: 'application/vnd.ms-excel' })
                    const link = document.createElement('a')
                    link.href = URL.createObjectURL(blob)
                    link.download = '安全管理人员证书列表.xlsx'
                    link.click()
                    URL.revokeObjectURL(link.href)
                })
                .catch(error => {
                    console.error('导出失败:', error)
                    elError('导出失败')
                })
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
        
        // 查看证书详情
        view(row) {
            this.$alert(`
                <div>
                    <h3>${row.certificateName}</h3>
                    <p><strong>持证人姓名：</strong>${row.holderName}</p>
                    <p><strong>联系方式：</strong>${row.holderPhone || '无'}</p>
                    <p><strong>身份证号：</strong>${row.idCard}</p>
                    <p><strong>性别：</strong>${row.gender}</p>
                    <p><strong>取证类型：</strong>${this.getCertTypeText(row.certType)}</p>
                    <p><strong>岗位：</strong>${row.position || '无'}</p>
                    <p><strong>入职时间：</strong>${row.hireDate || '无'}</p>
                    <p><strong>发证单位：</strong>${row.issuer || '无'}</p>
                    <p><strong>主管领导：</strong>${row.leaderName ? row.leaderName + (row.leaderContact ? ' (' + row.leaderContact + ')' : '') : '无'}</p>
                    <p><strong>证书编码：</strong>${row.certCode || '无'}</p>
                    <p><strong>取证日期：</strong>${row.issueDate}</p>
                    <p><strong>复审日期：</strong>${row.reviewDate || '无'}</p>
                    <p><strong>预警状态：</strong>${row.alertStatus || '未设置'}</p>
                </div>
            `, '证书详情', {
                dangerouslyUseHTMLString: true
            })
        },
        
        // 删除证书
        del(row) {
            elConfirm(`确认删除该证书记录?`, '提示', {
                type: 'warning'
            }).then(() => {
                del(row.id).then(res => {
                    if (res.code === 200) {
                        elSuccess('删除成功')
                        this.refresh()
                    } else {
                        elError(res.msg || '删除失败')
                    }
                }).catch(error => {
                    console.error('删除失败:', error)
                    elError('删除失败')
                })
            }).catch(() => {})
        },
        
        // 提交表单
        submitForm() {
            this.$refs.form.validate(valid => {
                if (!valid) return
                
                this.submitting = true
                const apiMethod = this.dialogType === 'add' ? add : update
                
                apiMethod(this.form).then(res => {
                    if (res.code === 200) {
                        elSuccess(res.msg || '操作成功')
                        this.dialogVisible = false
                        this.refresh()
                    } else {
                        elError(res.msg || '操作失败')
                    }
                }).catch(error => {
                    console.error('提交失败:', error)
                    elError('操作失败')
                }).finally(() => {
                    this.submitting = false
                })
            })
        },
        
        // 取消表单
        cancelForm() {
            this.dialogVisible = false
        },
        
        // 关闭弹窗时重置表单
        closeDialog() {
            this.$refs.form.resetFields()
            this.form = {
                id: null,
                certificateName: '',
                holderName: '',
                holderPhone: '',
                idCard: '',
                gender: '男',
                certType: '',
                position: '',
                hireDate: '',
                issuer: '',
                leaderName: '',
                leaderContact: '',
                certCode: '',
                issueDate: '',
                reviewDate: '',
                alertStatus: '未设置',
                alertCount: 0
            }
        },
        
        // 获取取证类型文本
        getCertTypeText(type) {
            const typeMap = {
                'safety': '安全管理',
                'fire': '消防管理',
                'special': '特种作业',
                'other': '其他'
            }
            return typeMap[type] || type
        },
        
        // 获取预警状态类型
        getAlertStatusType(status) {
            const statusMap = {
                '未设置': '',
                '待预警': 'warning',
                '已提醒': 'info',
                '已关闭': 'info'
            }
            return statusMap[status] || ''
        },
        
        // 获取剩余天数样式
        getRemainingDaysClass(days) {
            if (days === null) return ''
            if (days < 0) return 'text-danger'
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