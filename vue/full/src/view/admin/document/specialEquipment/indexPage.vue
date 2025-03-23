<!-- 特种设备管理 -->
<template>
    <div>
        <div class="filter-container">
            <el-input
                v-model.trim="searchParams.certificateName"
                placeholder="证书名称"
                clearable
                class="filter-item"
                style="width: 200px"
            />
            <el-select
                v-model="searchParams.equipmentType"
                placeholder="设备类型"
                clearable
                class="filter-item"
                style="width: 150px"
            >
                <el-option label="压力容器" value="压力容器"/>
                <el-option label="起重机械" value="起重机械"/>
                <el-option label="电梯" value="电梯"/>
                <el-option label="客运索道" value="客运索道"/>
                <el-option label="大型游乐设施" value="大型游乐设施"/>
                <el-option label="锅炉" value="锅炉"/>
                <el-option label="气瓶" value="气瓶"/>
                <el-option label="其他" value="其他"/>
            </el-select>
            <el-input
                v-model.trim="searchParams.registerNo"
                placeholder="注册编号"
                clearable
                class="filter-item"
                style="width: 200px"
            />
            
            <el-button class="filter-item" type="primary" @click="refresh(true)">查询</el-button>
            <el-button v-if="canAdd" class="filter-item" plain @click="add">新增</el-button>
            <el-button v-if="canExport" class="filter-item" plain @click="exportData">导出</el-button>
            
            <div class="search-toggle" @click="showAdvancedSearch = !showAdvancedSearch">
                {{ showAdvancedSearch ? '收起' : '展开' }}高级筛选
                <i :class="showAdvancedSearch ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"></i>
            </div>

            <div v-if="showAdvancedSearch" class="advanced-search">
                <el-input
                    v-model.trim="searchParams.managerName"
                    placeholder="设备管理人员"
                    clearable
                    class="filter-item"
                    style="width: 200px"
                />
                <el-input
                    v-model.trim="searchParams.manufacturer"
                    placeholder="制造厂家"
                    clearable
                    class="filter-item"
                    style="width: 200px"
                />
                <el-select
                    v-model="searchParams.status"
                    placeholder="设备状态"
                    clearable
                    class="filter-item"
                    style="width: 150px"
                >
                    <el-option label="正常" value="正常"/>
                    <el-option label="维修" value="维修"/>
                    <el-option label="停用" value="停用"/>
                </el-select>
                <el-input
                    v-model.trim="searchParams.installLocation"
                    placeholder="安装位置"
                    clearable
                    class="filter-item"
                    style="width: 200px"
                />
                <el-date-picker
                    v-model="searchParams.useDateStart"
                    type="date"
                    placeholder="使用日期开始"
                    value-format="yyyy-MM-dd"
                    class="filter-item"
                    style="width: 180px"
                />
                <el-date-picker
                    v-model="searchParams.useDateEnd"
                    type="date"
                    placeholder="使用日期结束"
                    value-format="yyyy-MM-dd"
                    class="filter-item"
                    style="width: 180px"
                />
                <el-date-picker
                    v-model="searchParams.recheckDateStart"
                    type="date"
                    placeholder="复检日期开始"
                    value-format="yyyy-MM-dd"
                    class="filter-item"
                    style="width: 180px"
                />
                <el-date-picker
                    v-model="searchParams.recheckDateEnd"
                    type="date"
                    placeholder="复检日期结束"
                    value-format="yyyy-MM-dd"
                    class="filter-item"
                    style="width: 180px"
                />
                <el-select
                    v-model="searchParams.alertStatus"
                    placeholder="预警状态"
                    clearable
                    class="filter-item"
                    style="width: 150px"
                >
                    <el-option label="未设置" value="未设置"/>
                    <el-option label="待预警" value="待预警"/>
                    <el-option label="已提醒" value="已提醒"/>
                    <el-option label="已关闭" value="已关闭"/>
                </el-select>
                <el-select
                    v-model="searchParams.checkStatus"
                    placeholder="校验状态"
                    clearable
                    class="filter-item"
                    style="width: 150px"
                >
                    <el-option label="正常" value="正常"/>
                    <el-option label="即将过期" value="即将过期"/>
                    <el-option label="已过期" value="已过期"/>
                </el-select>
            </div>
        </div>

        <!-- 数据表格 -->
        <el-table v-loading="loading" :data="data" border>
            <el-table-column type="index" width="50"/>
            <el-table-column label="证书名称" prop="certificateName" min-width="150"/>
            <el-table-column label="设备类型" prop="equipmentType" width="120"/>
            <el-table-column label="规格" prop="specifications" width="120"/>
            <el-table-column label="出厂编号" prop="factoryNo" width="120"/>
            <el-table-column label="注册编号" prop="registerNo" width="150"/>
            <el-table-column label="管理人员" prop="managerName" width="120"/>
            <el-table-column label="制造厂家" prop="manufacturer" min-width="150"/>
            <el-table-column label="设备状态" width="100" align="center">
                <template slot-scope="{row}">
                    <el-tag :type="getStatusType(row.status)">
                        {{ row.status || '未设置' }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column label="使用日期" prop="useDate" width="100"/>
            <el-table-column label="复检日期" prop="recheckDate" width="100"/>
            <el-table-column label="剩余天数" width="120" align="center">
                <template slot-scope="{row}">
                    <span :class="getRemainingDaysClass(row.remainingDays)">
                        {{ getRemainingDaysText(row.remainingDays) }}
                    </span>
                </template>
            </el-table-column>
            <el-table-column label="校验状态" width="100" align="center">
                <template slot-scope="{row}">
                    <el-tag :type="row.checkStatus === '已过期' ? 'danger' : (row.checkStatus === '即将过期' ? 'warning' : 'success')">
                        {{ row.checkStatus }}
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
            <el-table-column label="操作" width="280" align="center" fixed="right">
                <template slot-scope="{row}">
                    <el-button v-if="canView" type="text" size="mini" @click="view(row)">查看</el-button>
                    <el-button v-if="canUpdate" type="text" size="mini" @click="edit(row)">编辑</el-button>
                    <el-button v-if="canDelete" type="text" size="mini" @click="del(row)">删除</el-button>
                    <el-button v-if="canUpdate" type="text" size="mini" @click="openAlertSetting(row)">预警设置</el-button>
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
            :page-sizes="[10, 20, 30, 50]"
            class="pagination"
            @size-change="refresh()"
            @current-change="refresh()"
        />

        <!-- 新增/编辑弹窗 -->
        <el-dialog
            :title="dialogType === 'add' ? '新增特种设备' : '编辑特种设备'"
            :visible.sync="dialogVisible"
            width="700px"
            @closed="closeDialog"
        >
            <el-form ref="form" :model="form" :rules="rules" label-width="120px">
                <el-form-item label="证书名称" prop="certificateName">
                    <el-input v-model.trim="form.certificateName" placeholder="请输入证书名称"/>
                </el-form-item>
                <el-form-item label="设备类型" prop="equipmentType">
                    <el-select v-model="form.equipmentType" placeholder="请选择设备类型" style="width: 100%">
                        <el-option label="压力容器" value="压力容器"/>
                        <el-option label="起重机械" value="起重机械"/>
                        <el-option label="电梯" value="电梯"/>
                        <el-option label="客运索道" value="客运索道"/>
                        <el-option label="大型游乐设施" value="大型游乐设施"/>
                        <el-option label="锅炉" value="锅炉"/>
                        <el-option label="气瓶" value="气瓶"/>
                        <el-option label="其他" value="其他"/>
                    </el-select>
                </el-form-item>
                <el-form-item label="规格" prop="specifications">
                    <el-input v-model.trim="form.specifications" placeholder="请输入规格"/>
                </el-form-item>
                <el-form-item label="出厂编号" prop="factoryNo">
                    <el-input v-model.trim="form.factoryNo" placeholder="请输入出厂编号"/>
                </el-form-item>
                <el-form-item label="注册编号" prop="registerNo">
                    <el-input v-model.trim="form.registerNo" placeholder="请输入注册编号"/>
                </el-form-item>
                <el-form-item label="使用证书" prop="usageCert">
                    <el-input v-model.trim="form.usageCert" placeholder="请输入使用证书"/>
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
                <el-form-item label="领导联系方式" prop="leaderContact">
                    <el-input v-model.trim="form.leaderContact" placeholder="请输入领导联系方式"/>
                </el-form-item>
                <el-form-item label="制造厂家" prop="manufacturer">
                    <el-input v-model.trim="form.manufacturer" placeholder="请输入制造厂家"/>
                </el-form-item>
                <el-form-item label="设备状态" prop="status">
                    <el-select v-model="form.status" placeholder="请选择设备状态" style="width: 100%">
                        <el-option label="正常" value="正常"/>
                        <el-option label="维修" value="维修"/>
                        <el-option label="停用" value="停用"/>
                    </el-select>
                </el-form-item>
                <el-form-item label="安装位置" prop="installLocation">
                    <el-input v-model.trim="form.installLocation" placeholder="请输入安装位置"/>
                </el-form-item>
                <el-form-item label="使用日期" prop="useDate">
                    <el-date-picker
                        v-model="form.useDate"
                        type="date"
                        placeholder="选择使用日期"
                        value-format="yyyy-MM-dd"
                        style="width: 100%"
                    />
                </el-form-item>
                <el-form-item label="初次校验时间" prop="firstCheck">
                    <el-date-picker
                        v-model="form.firstCheck"
                        type="date"
                        placeholder="选择初次校验时间"
                        value-format="yyyy-MM-dd"
                        style="width: 100%"
                    />
                </el-form-item>
                <el-form-item label="最近校验时间" prop="lastCheck">
                    <el-date-picker
                        v-model="form.lastCheck"
                        type="date"
                        placeholder="选择最近校验时间"
                        value-format="yyyy-MM-dd"
                        style="width: 100%"
                    />
                </el-form-item>
                <el-form-item label="复检日期" prop="recheckDate">
                    <el-date-picker
                        v-model="form.recheckDate"
                        type="date"
                        placeholder="选择复检日期"
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
    </div>
</template>

<script>
import * as api from '@/api/doc/specialEquipment'
import { wic } from '@/util/auth'

export default {
    name: 'SpecialEquipment',
    
    data() {
        return {
            // 权限控制
            canAdd: wic('specialEquipment:add'),
            canView: wic('specialEquipment:view'),
            canUpdate: wic('specialEquipment:update'),
            canDelete: wic('specialEquipment:delete'),
            canExport: wic('specialEquipment:export'),
            
            // 加载状态
            loading: false,
            
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
                equipmentType: '',
                registerNo: '',
                managerName: '',
                manufacturer: '',
                status: '',
                installLocation: '',
                useDateStart: '',
                useDateEnd: '',
                recheckDateStart: '',
                recheckDateEnd: '',
                alertStatus: '',
                checkStatus: ''
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
                status: '正常',
                installLocation: '',
                useDate: '',
                firstCheck: '',
                lastCheck: '',
                recheckDate: '',
                alertStatus: '未设置',
                alertCount: 0
            },
            
            // 表单验证规则
            rules: {
                certificateName: [
                    { required: true, message: '请输入证书名称', trigger: 'blur' }
                ],
                equipmentType: [
                    { required: true, message: '请选择设备类型', trigger: 'change' }
                ],
                status: [
                    { required: true, message: '请选择设备状态', trigger: 'change' }
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
            api.search(params)
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
        
        // 新增设备
        add() {
            this.dialogType = 'add'
            // 确保预警状态默认值为"未设置"
            this.form.alertStatus = '未设置'
            this.form.alertCount = 0
            this.form.status = '正常'
            this.dialogVisible = true
        },
        
        // 查看设备详情
        view(row) {
            this.$alert(`
                <div>
                    <h3>${row.certificateName}</h3>
                    <p><strong>设备类型：</strong>${row.equipmentType}</p>
                    <p><strong>规格：</strong>${row.specifications || '无'}</p>
                    <p><strong>出厂编号：</strong>${row.factoryNo || '无'}</p>
                    <p><strong>注册编号：</strong>${row.registerNo || '无'}</p>
                    <p><strong>使用证书：</strong>${row.usageCert || '无'}</p>
                    <p><strong>管理人员：</strong>${row.managerName ? row.managerName + (row.managerContact ? ' (' + row.managerContact + ')' : '') : '无'}</p>
                    <p><strong>主管领导：</strong>${row.leaderName ? row.leaderName + (row.leaderContact ? ' (' + row.leaderContact + ')' : '') : '无'}</p>
                    <p><strong>制造厂家：</strong>${row.manufacturer || '无'}</p>
                    <p><strong>设备状态：</strong>${row.status || '未设置'}</p>
                    <p><strong>安装位置：</strong>${row.installLocation || '无'}</p>
                    <p><strong>使用日期：</strong>${row.useDate || '无'}</p>
                    <p><strong>初次校验时间：</strong>${row.firstCheck || '无'}</p>
                    <p><strong>最近校验时间：</strong>${row.lastCheck || '无'}</p>
                    <p><strong>复检日期：</strong>${row.recheckDate || '无'}</p>
                    <p><strong>预警状态：</strong>${row.alertStatus || '未设置'}</p>
                    <p><strong>提醒次数：</strong>${row.alertCount || 0}</p>
                    <p><strong>校验状态：</strong>${row.checkStatus || '未设置'}</p>
                </div>
            `, '设备详情', {
                dangerouslyUseHTMLString: true
            })
        },
        
        // 编辑设备
        edit(row) {
            this.dialogType = 'edit'
            this.form = JSON.parse(JSON.stringify(row))
            this.dialogVisible = true
        },
        
        // 删除设备
        del(row) {
            this.$confirm(`确认删除该设备记录?`, '提示', {
                type: 'warning'
            }).then(() => {
                api.del(row.id)
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
                const method = this.dialogType === 'add' ? api.add : api.update
                
                // 发送请求
                method(formData)
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
                status: '正常',
                installLocation: '',
                useDate: '',
                firstCheck: '',
                lastCheck: '',
                recheckDate: '',
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
            api.exportEquipment(params)
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
                    let filename = '特种设备列表.xlsx'
                    
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
        
        // 获取设备状态样式类型
        getStatusType(status) {
            const statusMap = {
                '正常': 'success',
                '维修': 'warning',
                '停用': 'danger'
            }
            return statusMap[status] || 'info'
        },
        
        // 获取剩余天数文本
        getRemainingDaysText(days) {
            if (days === null || days === undefined) {
                return '未设置复检日期'
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
                
                // 发送请求
                api.updateAlert(this.alertForm.id, formData)
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