<!-- 特种设备操作作业人员证书管理 -->
<template>
    <div class="app-container doc-page-container">
        <el-card shadow="hover" style="height: 100%">
            <div slot="header">
                <el-form :inline="true" size="mini" @submit.native.prevent>
                    <el-form-item>
                        <el-input
                            v-model.trim="searchParams.certificateName"
                            placeholder="证书名称"
                            clearable
                        />
                    </el-form-item>
                    <el-form-item>
                        <el-input
                            v-model.trim="searchParams.workerName"
                            placeholder="持证人员姓名"
                            clearable
                        />
                    </el-form-item>
                    <el-form-item>
                        <el-input
                            v-model.trim="searchParams.idCard"
                            placeholder="身份证号码"
                            clearable
                        />
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="searchParams.certType"
                            placeholder="取证种类"
                            clearable
                        >
                            <el-option label="起重机操作证" value="起重机操作证"/>
                            <el-option label="叉车操作证" value="叉车操作证"/>
                            <el-option label="锅炉操作证" value="锅炉操作证"/>
                            <el-option label="压力容器操作证" value="压力容器操作证"/>
                            <el-option label="电梯操作证" value="电梯操作证"/>
                            <el-option label="其他" value="其他"/>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button-group>
                            <el-button type="primary" icon="el-icon-search" @click="refresh(true)">查询</el-button>
                            <el-button v-if="canAdd" type="primary" icon="el-icon-plus" @click="add">新增</el-button>
                            <el-button v-if="canExport" type="primary" icon="el-icon-download" @click="exportData">导出</el-button>
                            <el-button type="primary" icon="el-icon-more" @click="showAdvancedSearch = !showAdvancedSearch">
                                {{ showAdvancedSearch ? '收起' : '展开' }}
                            </el-button>
                        </el-button-group>
                    </el-form-item>
                    
                    <!-- 高级筛选 -->
                    <template v-if="showAdvancedSearch">
                        <el-form-item>
                            <el-input
                                v-model.trim="searchParams.issuer"
                                placeholder="发证单位"
                                clearable
                            />
                        </el-form-item>
                        <el-form-item>
                            <el-date-picker
                                v-model="searchParams.issueDateStart"
                                type="date"
                                placeholder="取证时间开始"
                                value-format="yyyy-MM-dd"
                            />
                        </el-form-item>
                        <el-form-item>
                            <el-date-picker
                                v-model="searchParams.issueDateEnd"
                                type="date"
                                placeholder="取证时间结束"
                                value-format="yyyy-MM-dd"
                            />
                        </el-form-item>
                    </template>
                </el-form>
            </div>

            <!-- 数据表格 -->
            <el-table 
                v-loading="loading" 
                :data="data" 
                :height="tableHeight" 
                style="width: 100%"
            >
                <el-table-column type="index" width="50"/>
                <el-table-column label="证书名称" prop="certificateName" min-width="150"/>
                <el-table-column label="持证人员" prop="workerName" width="120"/>
                <el-table-column label="联系方式" prop="workerContact" width="120"/>
                <el-table-column label="身份证号" prop="idCard" width="170"/>
                <el-table-column label="取证种类" prop="certType" width="150">
                    <template slot-scope="{row}">
                        {{ getCertTypeText(row.certType) }}
                    </template>
                </el-table-column>
                <el-table-column label="发证单位" prop="issuer" min-width="150"/>
                <el-table-column label="取证时间" prop="issueDate" width="100"/>
                <el-table-column label="复审时间" prop="reviewDate" width="100"/>
                <el-table-column label="剩余天数" width="120" align="center">
                    <template slot-scope="{row}">
                        <span :class="getRemainingDaysClass(row.remainingDays)">
                            {{ getRemainingDaysText(row.remainingDays) }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column label="证书状态" width="100" align="center">
                    <template slot-scope="{row}">
                        <el-tag :type="row.status === '已过期' ? 'danger' : (row.status === '即将过期' ? 'warning' : 'success')">
                            {{ row.status }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="预警状态" width="100" align="center">
                    <template slot-scope="{row}">
                        <el-tag :type="getAlertStatusType(row.alertStatus)">
                            {{ row.alertStatus || '未设置' }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="180" align="center" fixed="right">
                    <template slot-scope="{row}">
                        <el-button-group>
                            <el-button v-if="canUpdate" type="primary" size="mini" @click="edit(row)">编辑</el-button>
                            <el-button v-if="canDelete" type="danger" size="mini" @click="del(row)">删除</el-button>
                            <el-dropdown v-if="canView || canUpdate" trigger="click" @command="handleCommand($event, row)">
                                <el-button type="primary" size="mini">
                                    更多<i class="el-icon-arrow-down el-icon--right"></i>
                                </el-button>
                                <el-dropdown-menu slot="dropdown">
                                    <el-dropdown-item v-if="canView" command="view">查看详情</el-dropdown-item>
                                    <el-dropdown-item v-if="canUpdate" command="alert">预警设置</el-dropdown-item>
                                </el-dropdown-menu>
                            </el-dropdown>
                        </el-button-group>
                    </template>
                </el-table-column>
            </el-table>

            <div v-if="data.length === 0 && !loading" class="empty-data">
                没有找到匹配的数据
            </div>

            <el-pagination
                v-if="total > 0 || data.length > 0"
                :current-page.sync="page.current"
                :page-size.sync="page.size"
                :total="total"
                layout="total, sizes, prev, pager, next, jumper"
                :page-sizes="[10, 20, 50, 100]"
                style="margin-top: 15px; text-align: right"
                background
                @size-change="refresh()"
                @current-change="refresh()"
            />

            <!-- 新增/编辑弹窗 -->
            <el-dialog
                :title="dialogType === 'add' ? '新增证书' : '编辑证书'"
                :visible.sync="dialogVisible"
                width="700px"
                @closed="closeDialog"
            >
                <el-form ref="form" :model="form" :rules="rules" label-width="120px">
                    <el-form-item label="证书名称" prop="certificateName">
                        <el-input v-model.trim="form.certificateName" placeholder="请输入证书名称"/>
                    </el-form-item>
                    <el-form-item label="持证人员姓名" prop="workerName">
                        <el-input v-model.trim="form.workerName" placeholder="请输入持证人员姓名"/>
                    </el-form-item>
                    <el-form-item label="联系方式" prop="workerContact">
                        <el-input v-model.trim="form.workerContact" placeholder="请输入联系方式"/>
                    </el-form-item>
                    <el-form-item label="身份证号码" prop="idCard">
                        <el-input v-model.trim="form.idCard" placeholder="请输入身份证号码"/>
                    </el-form-item>
                    <el-form-item label="性别" prop="gender">
                        <el-radio-group v-model="form.gender">
                            <el-radio label="男"/>
                            <el-radio label="女"/>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="取证种类" prop="certType">
                        <el-select v-model="form.certType" placeholder="请选择取证种类" style="width: 100%">
                            <el-option label="起重机操作证" value="起重机操作证"/>
                            <el-option label="叉车操作证" value="叉车操作证"/>
                            <el-option label="锅炉操作证" value="锅炉操作证"/>
                            <el-option label="压力容器操作证" value="压力容器操作证"/>
                            <el-option label="电梯操作证" value="电梯操作证"/>
                            <el-option label="其他" value="其他"/>
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
                    <el-form-item label="领导联系方式" prop="leaderContact">
                        <el-input v-model.trim="form.leaderContact" placeholder="请输入领导联系方式"/>
                    </el-form-item>
                    <el-form-item label="操作证编码" prop="operationCode">
                        <el-input v-model.trim="form.operationCode" placeholder="请输入操作证编码"/>
                    </el-form-item>
                    <el-form-item label="取证时间" prop="issueDate">
                        <el-date-picker
                            v-model="form.issueDate"
                            type="date"
                            placeholder="选择取证时间"
                            value-format="yyyy-MM-dd"
                            style="width: 100%"
                        />
                    </el-form-item>
                    <el-form-item label="复审时间" prop="reviewDate">
                        <el-date-picker
                            v-model="form.reviewDate"
                            type="date"
                            placeholder="选择复审时间"
                            value-format="yyyy-MM-dd"
                            style="width: 100%"
                        />
                    </el-form-item>
                    <el-form-item v-if="dialogType === 'edit'" label="预警状态" prop="alertStatus">
                        <el-select v-model="form.alertStatus" placeholder="请选择预警状态" style="width: 100%">
                            <el-option label="未设置" value="未设置"/>
                            <el-option label="待预警" value="待预警"/>
                            <el-option label="已提醒" value="已提醒"/>
                            <el-option label="已关闭" value="已关闭"/>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" :loading="submitting" @click="submitForm">提交</el-button>
                        <el-button @click="dialogVisible = false">取消</el-button>
                    </el-form-item>
                </el-form>
            </el-dialog>

            <!-- 预警设置弹窗 -->
            <el-dialog
                title="预警设置"
                :visible.sync="alertDialogVisible"
                width="500px"
                @closed="closeAlertDialog"
            >
                <el-form ref="alertForm" :model="alertForm" :rules="alertRules" label-width="120px">
                    <el-form-item label="预警状态" prop="alertStatus">
                        <el-select v-model="alertForm.alertStatus" placeholder="请选择预警状态" style="width: 100%">
                            <el-option label="未设置" value="未设置"/>
                            <el-option label="待预警" value="待预警"/>
                            <el-option label="已提醒" value="已提醒"/>
                            <el-option label="已关闭" value="已关闭"/>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="提醒次数" prop="alertCount">
                        <el-input-number v-model="alertForm.alertCount" :min="0" :max="100" controls-position="right"/>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" :loading="alertSubmitting" @click="submitAlertForm">保存</el-button>
                        <el-button @click="cancelAlertForm">取消</el-button>
                    </el-form-item>
                </el-form>
            </el-dialog>
        </el-card>
    </div>
</template>

<script>
import request from '@/api/request'
import { wic } from '@/util/auth'

export default {
    name: 'SpecialEquipmentWorkerCert',
    
    data() {
        return {
            // 权限控制
            canAdd: wic('specialEquipmentWorkerCert:add'),
            canView: wic('specialEquipmentWorkerCert:view'),
            canUpdate: wic('specialEquipmentWorkerCert:update'),
            canDelete: wic('specialEquipmentWorkerCert:delete'),
            canExport: wic('specialEquipmentWorkerCert:export'),
            
            // 加载状态
            loading: false,
            tableHeight: 'calc(100vh - 280px)',
            
            // 数据列表
            data: [],
            total: 0,
            
            // 分页参数
            page: {
                current: 1,
                size: 10
            },
            
            // 搜索参数
            searchParams: {
                certificateName: '',
                workerName: '',
                idCard: '',
                certType: '',
                issuer: '',
                issueDateStart: '',
                issueDateEnd: '',
                reviewDateStart: '',
                reviewDateEnd: '',
                alertStatus: '',
                status: ''
            },
            
            // 是否显示高级搜索
            showAdvancedSearch: false,
            
            // 对话框控制
            dialogVisible: false,
            dialogType: 'add',
            submitting: false,
            
            // 表单数据
            form: {
                id: null,
                certificateName: '',
                workerName: '',
                workerContact: '',
                idCard: '',
                gender: '男',
                certType: '',
                position: '',
                hireDate: '',
                issuer: '',
                leaderName: '',
                leaderContact: '',
                operationCode: '',
                issueDate: '',
                reviewDate: '',
                alertStatus: '未设置',
                alertCount: 0
            },
            
            // 表单验证规则
            rules: {
                certificateName: [
                    { required: true, message: '请输入证书名称', trigger: 'blur' }
                ],
                workerName: [
                    { required: true, message: '请输入持证人员姓名', trigger: 'blur' }
                ],
                idCard: [
                    { required: true, message: '请输入身份证号码', trigger: 'blur' }
                ],
                certType: [
                    { required: true, message: '请选择取证种类', trigger: 'change' }
                ],
                issueDate: [
                    { required: true, message: '请选择取证时间', trigger: 'change' }
                ]
            },
            
            // 预警设置相关数据
            alertDialogVisible: false,
            alertSubmitting: false,
            alertForm: {
                id: null,
                alertStatus: '未设置',
                alertCount: 0
            },
            alertRules: {
                alertStatus: [
                    { required: true, message: '请选择预警状态', trigger: 'change' }
                ]
            }
        }
    },
    
    created() {
        this.refresh()
    },
    
    methods: {
        // 处理API响应数据，统一返回格式
        processResponseData(response) {
            if (!response) return { data: [], total: 0 };

            // 检查是否是标准格式，包含 status 和 data
            if (response.hasOwnProperty('status') && response.hasOwnProperty('data')) {
                if (response.status === 200) {
                    let responseData = response.data;

                    // 检查是否是MyBatis-Plus的分页格式
                    if (responseData && responseData.hasOwnProperty('records') && 
                        (responseData.hasOwnProperty('total') || responseData.hasOwnProperty('current'))) {
                        
                        // 特殊情况处理：当total为0但records有数据时
                        if ((responseData.total === 0 || responseData.total === undefined) && 
                            responseData.records && responseData.records.length > 0) {
                            console.log('检测到total为0但records有数据，使用records.length作为total');
                            return { 
                                data: responseData.records, 
                                total: responseData.records.length 
                            };
                        }

                        return { 
                            data: responseData.records || [], 
                            total: responseData.total || 0 
                        };
                    }

                    // 如果直接返回数组
                    if (Array.isArray(responseData)) {
                        return { data: responseData, total: responseData.length };
                    }

                    // 其他情况，尝试使用data作为数据源
                    return { 
                        data: responseData || [], 
                        total: Array.isArray(responseData) ? responseData.length : 0 
                    };
                }
            }

            // 默认返回空数据
            return { data: [], total: 0 };
        },
        
        // 刷新列表数据
        refresh(resetPage = false) {
            if (resetPage) {
                this.page.current = 1
            }
            
            this.loading = true
            
            // 构建查询参数
            const params = {
                page: this.page.current,
                pageSize: this.page.size,
                ...this.searchParams
            }
            
            console.log('查询参数:', JSON.stringify(params))
            // 调用后端接口
            request.post('/admin/document/specialEquipmentWorker/search', params)
                .then(response => {
                    console.log('完整响应对象:', response)
                    
                    // 处理响应数据
                    const result = this.processResponseData(response)
                    console.log('处理后的结果:', result)
                    
                    this.data = result.data || []
                    this.total = result.total || 0
                    
                    if (this.data.length === 0) {
                        console.warn('查询成功但无数据')
                    } else {
                        console.log(`成功获取${this.data.length}条数据，总数：${this.total}`)
                    }
                })
                .catch(error => {
                    console.error('获取数据失败', error)
                    this.$message.error('获取数据失败')
                    this.data = []
                    this.total = 0
                })
                .finally(() => {
                    this.loading = false
                })
        },
        
        // 新增证书
        add() {
            this.dialogType = 'add'
            // 确保预警状态默认值为"未设置"
            this.form.alertStatus = '未设置'
            this.form.alertCount = 0
            this.dialogVisible = true
        },
        
        // 查看证书详情
        view(row) {
            this.$alert(`
                <div>
                    <h3>${row.certificateName}</h3>
                    <p><strong>持证人员姓名：</strong>${row.workerName}</p>
                    <p><strong>持证人员联系方式：</strong>${row.workerContact || '无'}</p>
                    <p><strong>身份证号码：</strong>${row.idCard}</p>
                    <p><strong>性别：</strong>${row.gender}</p>
                    <p><strong>取证种类：</strong>${this.getCertTypeText(row.certType)}</p>
                    <p><strong>岗位：</strong>${row.position || '无'}</p>
                    <p><strong>入职时间：</strong>${row.hireDate || '无'}</p>
                    <p><strong>发证单位：</strong>${row.issuer || '无'}</p>
                    <p><strong>主管领导：</strong>${row.leaderName ? row.leaderName + (row.leaderContact ? ' (' + row.leaderContact + ')' : '') : '无'}</p>
                    <p><strong>操作证编码：</strong>${row.operationCode || '无'}</p>
                    <p><strong>取证时间：</strong>${row.issueDate}</p>
                    <p><strong>复审时间：</strong>${row.reviewDate || '无'}</p>
                    <p><strong>预警状态：</strong>${row.alertStatus || '未设置'}</p>
                    <p><strong>提醒次数：</strong>${row.alertCount || 0}</p>
                    <p><strong>证书状态：</strong>${row.status || '未设置'}</p>
                </div>
            `, '证书详情', {
                dangerouslyUseHTMLString: true
            })
        },
        
        // 编辑证书
        edit(row) {
            this.dialogType = 'edit'
            this.form = JSON.parse(JSON.stringify(row))
            this.dialogVisible = true
        },
        
        // 删除证书
        del(row) {
            this.$confirm(`确认删除该证书记录?`, '提示', {
                type: 'warning'
            }).then(() => {
                request.delete(`/admin/document/specialEquipmentWorker/${row.id}`)
                    .then(response => {
                        console.log('删除响应:', response)
                        if (response && response.status === 200) {
                            this.$message.success('删除成功')
                            this.refresh()
                        } else {
                            this.$message.error('删除失败：' + (response.data?.msg || '未知错误'))
                        }
                    })
                    .catch(error => {
                        console.error('删除失败', error)
                        this.$message.error('删除失败')
                    })
            }).catch(() => {})
        },
        
        // 提交表单
        submitForm() {
            this.$refs.form.validate(valid => {
                if (!valid) return
                
                this.submitting = true
                
                // 创建一个深拷贝，避免直接修改表单
                const formData = JSON.parse(JSON.stringify(this.form))
                
                // 确保新增时预警相关字段有默认值
                if (this.dialogType === 'add') {
                    formData.alertStatus = '未设置'
                    formData.alertCount = 0
                }
                
                // 确定提交方法和URL
                const method = this.dialogType === 'add' ? 'post' : 'put'
                const url = '/admin/document/specialEquipmentWorker'
                
                // 发送请求
                request({
                    method: method,
                    url: url,
                    data: formData
                })
                    .then(response => {
                        console.log('提交响应:', response)
                        this.submitting = false
                        
                        if (response && response.status === 200) {
                            this.$message.success('操作成功')
                            this.dialogVisible = false
                            this.refresh()
                        } else {
                            this.$message.error('操作失败：' + (response.data?.msg || '未知错误'))
                        }
                    })
                    .catch(error => {
                        console.error('操作失败', error)
                        this.$message.error('操作失败: ' + (error.message || JSON.stringify(error)))
                        this.submitting = false
                    })
            })
        },
        
        // 关闭弹窗时重置表单
        closeDialog() {
            this.$refs.form.resetFields()
            this.form = {
                id: null,
                certificateName: '',
                workerName: '',
                workerContact: '',
                idCard: '',
                gender: '男',
                certType: '',
                position: '',
                hireDate: '',
                issuer: '',
                leaderName: '',
                leaderContact: '',
                operationCode: '',
                issueDate: '',
                reviewDate: '',
                alertStatus: '未设置',
                alertCount: 0
            }
        },
        
        // 导出数据
        exportData() {
            // 构建查询参数
            const params = {
                ...this.searchParams
            }
            
            // 显示加载提示
            this.$message.info('正在准备导出数据，请稍候...')
            
            // 使用axios直接下载文件
            request({
                url: '/admin/document/specialEquipmentWorker/export',
                method: 'post',
                data: params,
                responseType: 'blob'
            })
            .then(response => {
                // 检查是否是blob类型数据
                if (!(response && response.data instanceof Blob)) {
                    console.error('导出失败: 返回数据不是文件格式');
                    this.$message.error('导出失败，返回数据格式不正确');
                    return;
                }

                // 创建blob对象
                const blob = new Blob([response.data])
                
                // 创建下载链接
                const downloadElement = document.createElement('a')
                const href = window.URL.createObjectURL(blob) // 创建下载的链接
                
                // 处理文件名
                let filename = '特种设备操作作业人员证书列表.xlsx'
                
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
        
        // 获取取证种类文本
        getCertTypeText(type) {
            switch (type) {
                case '起重机操作证': return '起重机操作证'
                case '叉车操作证': return '叉车操作证'
                case '锅炉操作证': return '锅炉操作证'
                case '压力容器操作证': return '压力容器操作证'
                case '电梯操作证': return '电梯操作证'
                case '其他': return '其他'
                default: return type || '未设置'
            }
        },
        
        // 获取剩余天数文本
        getRemainingDaysText(days) {
            if (days === null || days === undefined) {
                return '未设置复审日期'
            }
            
            if (days < 0) {
                return `已过期${Math.abs(days)}天`
            }
            
            return `剩余${days}天`
        },
        
        // 获取剩余天数样式类
        getRemainingDaysClass(days) {
            if (days === null || days === undefined) {
                return ''
            }
            
            if (days < 0) {
                return 'text-danger'
            } else if (days <= 30) {
                return 'text-warning'
            } else {
                return 'text-success'
            }
        },
        
        // 获取预警状态样式类型
        getAlertStatusType(status) {
            const statusMap = {
                '未设置': 'info',
                '待预警': 'warning',
                '已提醒': 'success',
                '已关闭': 'info'
            }
            return statusMap[status] || 'info'
        },
        
        // 打开预警设置
        openAlertSetting(row) {
            this.alertForm = {
                id: row.id,
                alertStatus: row.alertStatus,
                alertCount: row.alertCount
            }
            this.alertDialogVisible = true
        },
        
        // 提交预警设置
        submitAlertForm() {
            this.$refs.alertForm.validate(valid => {
                if (!valid) return
                
                this.alertSubmitting = true
                
                // 创建一个深拷贝，避免直接修改表单
                const formData = JSON.parse(JSON.stringify(this.alertForm))
                
                // 确定提交方法和URL
                const method = 'put'
                const url = `/admin/document/specialEquipmentWorker/${this.alertForm.id}/alert`
                
                // 发送请求
                request({
                    method: method,
                    url: url,
                    data: formData
                })
                    .then(response => {
                        console.log('提交响应:', response)
                        this.alertSubmitting = false
                        
                        if (response && response.status === 200) {
                            this.$message.success('预警设置成功')
                            this.alertDialogVisible = false
                            this.refresh()
                        } else {
                            this.$message.error('预警设置失败：' + (response.data?.msg || '未知错误'))
                        }
                    })
                    .catch(error => {
                        console.error('预警设置失败', error)
                        this.$message.error('预警设置失败: ' + (error.message || JSON.stringify(error)))
                        this.alertSubmitting = false
                    })
            })
        },
        
        // 取消预警设置
        cancelAlertForm() {
            this.alertDialogVisible = false
        },
        
        // 关闭预警设置弹窗时重置表单
        closeAlertDialog() {
            this.$refs.alertForm.resetFields()
            this.alertForm = {
                id: null,
                alertStatus: '未设置',
                alertCount: 0
            }
        },
        
        // 处理下拉菜单命令
        handleCommand(command, row) {
            switch (command) {
                case 'view':
                    this.view(row)
                    break
                case 'alert':
                    this.openAlertSetting(row)
                    break
            }
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
.filter-container {
    margin-bottom: 15px;
    display: flex;
    align-items: center;
    flex-wrap: wrap;
}
.filter-item {
    margin-right: 10px;
    margin-bottom: 10px;
}
.advanced-search {
    margin-top: 10px;
    padding-top: 10px;
    border-top: 1px dashed #dcdcdc;
}
.search-toggle {
    cursor: pointer;
    color: #409EFF;
    font-size: 14px;
    margin-bottom: 10px;
}
.search-toggle i {
    margin-left: 5px;
}
.empty-data {
    text-align: center;
    color: #909399;
    padding: 30px 0;
    font-size: 14px;
}
</style> 