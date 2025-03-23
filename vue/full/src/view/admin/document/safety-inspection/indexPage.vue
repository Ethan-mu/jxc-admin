<template>
    <div class="app-container doc-page-container">
        <!-- 搜索表单 -->
        <el-card shadow="hover" style="height: 100%">
            <div slot="header">
                <el-form :model="searchForm" :inline="true" size="mini" @submit.native.prevent>
                    <el-form-item label="检查人员">
                        <el-input v-model.trim="searchForm.inspector" placeholder="请输入检查人员" clearable/>
                    </el-form-item>
                    <el-form-item label="隐患等级">
                        <el-select v-model="searchForm.hazardLevel" placeholder="请选择隐患等级" clearable>
                            <el-option label="一般隐患" value="一般隐患"/>
                            <el-option label="重大隐患" value="重大隐患"/>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="整改负责人">
                        <el-input v-model.trim="searchForm.responsiblePerson" placeholder="请输入整改负责人" clearable/>
                    </el-form-item>
                    <el-form-item label="整改状态">
                        <el-select v-model="searchForm.isResolved" placeholder="请选择整改状态" clearable>
                            <el-option label="未整改" :value="0"/>
                            <el-option label="已整改" :value="1"/>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="预警状态">
                        <el-select v-model="searchForm.alertStatus" placeholder="请选择预警状态" clearable>
                            <el-option label="未设置" value="未设置"/>
                            <el-option label="待预警" value="待预警"/>
                            <el-option label="已提醒" value="已提醒"/>
                            <el-option label="已关闭" value="已关闭"/>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="检查状态">
                        <el-select v-model="searchForm.checkStatus" placeholder="请选择检查状态" clearable>
                            <el-option label="正常" value="正常"/>
                            <el-option label="即将过期" value="即将过期"/>
                            <el-option label="已过期" value="已过期"/>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="整改期限">
                        <el-date-picker
                            v-model="dateRange"
                            type="daterange"
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"
                            value-format="yyyy-MM-dd"
                            @change="handleDateRangeChange"
                        />
                    </el-form-item>
                    <el-form-item>
                        <el-button-group>
                            <el-button type="primary" icon="el-icon-search" @click="search">查询</el-button>
                            <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
                            <el-button type="primary" icon="el-icon-download" @click="handleExport">导出</el-button>
                            <el-button type="primary" icon="el-icon-s-data" @click="showStatistics">统计</el-button>
                        </el-button-group>
                    </el-form-item>
                </el-form>
            </div>

            <!-- 表格 -->
            <el-table
                v-loading="loading"
                :data="tableData"
                border
                size="mini"
                height="calc(100% - 70px)"
                style="width: 100%"
                @selection-change="handleSelectionChange"
            >
                <el-table-column type="selection" width="55" />
                <el-table-column prop="id" label="ID" width="60" />
                <el-table-column prop="inspector" label="检查人员" min-width="100" />
                <el-table-column prop="hazardDesc" label="隐患描述" min-width="180">
                    <template slot-scope="scope">
                        <el-tooltip :content="scope.row.hazardDesc" placement="top" effect="light">
                            <div class="ellipsis">{{ scope.row.hazardDesc }}</div>
                        </el-tooltip>
                    </template>
                </el-table-column>
                <el-table-column prop="hazardLevel" label="隐患等级" width="100">
                    <template slot-scope="scope">
                        <el-tag :type="scope.row.hazardLevel === '重大隐患' ? 'danger' : ''">
                            {{ scope.row.hazardLevel }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="correctiveAction" label="整改措施" min-width="180">
                    <template slot-scope="scope">
                        <el-tooltip :content="scope.row.correctiveAction" placement="top" effect="light">
                            <div class="ellipsis">{{ scope.row.correctiveAction }}</div>
                        </el-tooltip>
                    </template>
                </el-table-column>
                <el-table-column prop="deadline" label="整改期限" width="110" />
                <el-table-column prop="responsiblePerson" label="整改负责人" width="120" />
                <el-table-column prop="isResolved" label="整改状态" width="100">
                    <template slot-scope="scope">
                        <el-tag :type="scope.row.isResolved === 1 ? 'success' : 'warning'">
                            {{ scope.row.isResolved === 1 ? '已整改' : '未整改' }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="checkStatus" label="检查状态" width="100">
                    <template slot-scope="scope">
                        <el-tag :type="getCheckStatusType(scope.row.checkStatus)">
                            {{ scope.row.checkStatus }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="alertStatus" label="预警状态" width="100">
                    <template slot-scope="scope">
                        <el-tag :type="getAlertStatusType(scope.row.alertStatus)">
                            {{ scope.row.alertStatus }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="220" fixed="right">
                    <template slot-scope="scope">
                        <el-button type="primary" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
                        <el-button type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
                        <el-dropdown size="mini" split-button type="primary" @command="(command) => handleCommand(command, scope.row)">
                            更多
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item command="view">查看详情</el-dropdown-item>
                                <el-dropdown-item command="setAlert" :disabled="scope.row.alertStatus === '待预警'">设置预警</el-dropdown-item>
                                <el-dropdown-item command="closeAlert" :disabled="scope.row.alertStatus === '已关闭' || scope.row.alertStatus === '未设置'">关闭预警</el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </template>
                </el-table-column>
            </el-table>

            <!-- 分页 -->
            <el-pagination
                style="margin-top: 20px; text-align: right"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="searchForm.page"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="searchForm.pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total"
            />
        </el-card>

        <!-- 新增/编辑对话框 -->
        <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="650px">
            <el-form :model="form" :rules="rules" ref="form" label-width="120px">
                <el-form-item label="检查人员" prop="inspector">
                    <el-input v-model="form.inspector" placeholder="请输入检查人员"></el-input>
                </el-form-item>
                <el-form-item label="隐患描述" prop="hazardDesc">
                    <el-input v-model="form.hazardDesc" type="textarea" :rows="3" placeholder="请描述安全隐患"></el-input>
                </el-form-item>
                <el-form-item label="隐患等级" prop="hazardLevel">
                    <el-select v-model="form.hazardLevel" placeholder="请选择隐患等级" style="width: 100%">
                        <el-option label="一般隐患" value="一般隐患"></el-option>
                        <el-option label="重大隐患" value="重大隐患"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="整改措施" prop="correctiveAction">
                    <el-input v-model="form.correctiveAction" type="textarea" :rows="3" placeholder="请输入整改措施"></el-input>
                </el-form-item>
                <el-form-item label="整改期限" prop="deadline">
                    <el-date-picker v-model="form.deadline" type="date" placeholder="选择整改期限" style="width: 100%" value-format="yyyy-MM-dd"></el-date-picker>
                </el-form-item>
                <el-form-item label="检查负责人电话" prop="inspectorContact">
                    <el-input v-model="form.inspectorContact" placeholder="请输入检查负责人电话"></el-input>
                </el-form-item>
                <el-form-item label="整改资金来源" prop="fundSource">
                    <el-input v-model="form.fundSource" placeholder="请输入整改资金来源"></el-input>
                </el-form-item>
                <el-form-item label="整改负责人" prop="responsiblePerson">
                    <el-input v-model="form.responsiblePerson" placeholder="请输入整改负责人"></el-input>
                </el-form-item>
                <el-form-item label="整改负责人电话" prop="respPersonPhone">
                    <el-input v-model="form.respPersonPhone" placeholder="请输入整改负责人电话"></el-input>
                </el-form-item>
                <el-form-item label="隐患整改时间" prop="rectifyDate">
                    <el-date-picker v-model="form.rectifyDate" type="date" placeholder="选择隐患整改时间" style="width: 100%" value-format="yyyy-MM-dd"></el-date-picker>
                </el-form-item>
                <el-form-item label="验收负责人" prop="verifier">
                    <el-input v-model="form.verifier" placeholder="请输入验收负责人"></el-input>
                </el-form-item>
                <el-form-item label="验证时间" prop="verifyDate">
                    <el-date-picker v-model="form.verifyDate" type="date" placeholder="选择验证时间" style="width: 100%" value-format="yyyy-MM-dd"></el-date-picker>
                </el-form-item>
                <el-form-item label="公示情况" prop="publicityStatus">
                    <el-select v-model="form.publicityStatus" placeholder="请选择公示情况" style="width: 100%">
                        <el-option label="未公示" value="未公示"></el-option>
                        <el-option label="已公示" value="已公示"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="上报情况" prop="reportStatus">
                    <el-select v-model="form.reportStatus" placeholder="请选择上报情况" style="width: 100%">
                        <el-option label="未上报" value="未上报"></el-option>
                        <el-option label="已上报" value="已上报"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="是否已整改" prop="isResolved">
                    <el-switch v-model="form.isResolved" :active-value="1" :inactive-value="0" active-text="已整改" inactive-text="未整改"></el-switch>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitForm">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 详情对话框 -->
        <el-dialog title="隐患排查详情" :visible.sync="detailVisible" width="650px">
            <el-descriptions :column="2" border>
                <el-descriptions-item label="检查人员">{{ detailData.inspector }}</el-descriptions-item>
                <el-descriptions-item label="检查负责人电话">{{ detailData.inspectorContact }}</el-descriptions-item>
                <el-descriptions-item label="隐患描述" :span="2">{{ detailData.hazardDesc }}</el-descriptions-item>
                <el-descriptions-item label="隐患等级">
                    <el-tag :type="detailData.hazardLevel === '重大隐患' ? 'danger' : ''">{{ detailData.hazardLevel }}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="整改期限">{{ detailData.deadline }}</el-descriptions-item>
                <el-descriptions-item label="整改措施" :span="2">{{ detailData.correctiveAction }}</el-descriptions-item>
                <el-descriptions-item label="整改资金来源">{{ detailData.fundSource }}</el-descriptions-item>
                <el-descriptions-item label="整改负责人">{{ detailData.responsiblePerson }}</el-descriptions-item>
                <el-descriptions-item label="整改负责人电话">{{ detailData.respPersonPhone }}</el-descriptions-item>
                <el-descriptions-item label="整改状态">
                    <el-tag :type="detailData.isResolved === 1 ? 'success' : 'warning'">
                        {{ detailData.isResolved === 1 ? '已整改' : '未整改' }}
                    </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="隐患整改时间">{{ detailData.rectifyDate }}</el-descriptions-item>
                <el-descriptions-item label="验收负责人">{{ detailData.verifier }}</el-descriptions-item>
                <el-descriptions-item label="验证时间">{{ detailData.verifyDate }}</el-descriptions-item>
                <el-descriptions-item label="公示情况">{{ detailData.publicityStatus }}</el-descriptions-item>
                <el-descriptions-item label="上报情况">{{ detailData.reportStatus }}</el-descriptions-item>
                <el-descriptions-item label="预警状态">
                    <el-tag :type="getAlertStatusType(detailData.alertStatus)">{{ detailData.alertStatus }}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="预警次数">{{ detailData.alertCount }}</el-descriptions-item>
                <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
                <el-descriptions-item label="更新时间">{{ detailData.updateTime }}</el-descriptions-item>
            </el-descriptions>
        </el-dialog>

        <!-- 统计分析对话框 -->
        <el-dialog title="安全隐患统计分析" :visible.sync="statisticsVisible" width="800px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <div ref="levelChart" style="width: 100%; height: 300px;"></div>
                </el-col>
                <el-col :span="12">
                    <div ref="resolveChart" style="width: 100%; height: 300px;"></div>
                </el-col>
            </el-row>
            <el-row style="margin-top: 20px">
                <el-col :span="24">
                    <el-alert
                        title="即将到期隐患提醒"
                        type="warning"
                        show-icon
                    >
                        <template slot="title">
                            有 <span style="color: red; font-weight: bold">{{ urgentCount }}</span> 个隐患在7天内将到期，请及时处理！
                        </template>
                    </el-alert>
                </el-col>
            </el-row>
        </el-dialog>
    </div>
</template>

<script>
import api from '@/api/doc/safetyInspection'
import { download } from '@/util/file'
import { elSuccess, elError, elConfirm } from '@/util/message'

export default {
    name: 'SafetyInspection',
    data() {
        return {
            // 搜索表单
            searchForm: {
                page: 1,
                pageSize: 10,
                inspector: '',
                hazardLevel: '',
                responsiblePerson: '',
                isResolved: null,
                alertStatus: '',
                checkStatus: '',
                deadlineBegin: '',
                deadlineEnd: ''
            },
            // 日期范围选择器
            dateRange: [],
            // 表格数据
            tableData: [],
            // 选中行
            selectedRows: [],
            // 总数
            total: 0,
            // 加载状态
            loading: false,
            // 对话框
            dialogVisible: false,
            dialogTitle: '新增安全隐患排查',
            // 详情对话框
            detailVisible: false,
            detailData: {},
            // 表单数据
            form: this.initFormData(),
            // 表单验证规则
            rules: {
                inspector: [{ required: true, message: '请输入检查人员', trigger: 'blur' }],
                hazardDesc: [{ required: true, message: '请描述安全隐患', trigger: 'blur' }],
                hazardLevel: [{ required: true, message: '请选择隐患等级', trigger: 'change' }],
                correctiveAction: [{ required: true, message: '请输入整改措施', trigger: 'blur' }],
                deadline: [{ required: true, message: '请选择整改期限', trigger: 'change' }],
                responsiblePerson: [{ required: true, message: '请输入整改负责人', trigger: 'blur' }]
            },
            // 统计分析
            statisticsVisible: false,
            levelChart: null,
            resolveChart: null,
            urgentCount: 0
        }
    },
    created() {
        this.fetchData()
    },
    methods: {
        // 初始化表单数据
        initFormData() {
            return {
                id: null,
                inspector: '',
                hazardDesc: '',
                hazardLevel: '一般隐患',
                correctiveAction: '',
                deadline: '',
                inspectorContact: '',
                fundSource: '',
                responsiblePerson: '',
                respPersonPhone: '',
                rectifyDate: '',
                verifier: '',
                verifyDate: '',
                publicityStatus: '未公示',
                reportStatus: '未上报',
                isResolved: 0
            }
        },
        // 获取数据
        fetchData() {
            this.loading = true
            api.search(this.searchForm).then(response => {
                this.tableData = response.data.records || []
                this.total = response.data.total || 0
                this.loading = false
            }).catch(() => {
                this.loading = false
            })
        },
        // 搜索
        search() {
            this.searchForm.page = 1
            this.fetchData()
        },
        // 重置搜索
        resetSearch() {
            this.searchForm = {
                page: 1,
                pageSize: 10,
                inspector: '',
                hazardLevel: '',
                responsiblePerson: '',
                isResolved: null,
                alertStatus: '',
                checkStatus: '',
                deadlineBegin: '',
                deadlineEnd: ''
            }
            this.dateRange = []
            this.fetchData()
        },
        // 日期范围变化
        handleDateRangeChange(val) {
            if (val) {
                this.searchForm.deadlineBegin = val[0]
                this.searchForm.deadlineEnd = val[1]
            } else {
                this.searchForm.deadlineBegin = ''
                this.searchForm.deadlineEnd = ''
            }
        },
        // 处理页面大小变化
        handleSizeChange(val) {
            this.searchForm.pageSize = val
            this.fetchData()
        },
        // 处理页码变化
        handleCurrentChange(val) {
            this.searchForm.page = val
            this.fetchData()
        },
        // 处理表格选择变化
        handleSelectionChange(val) {
            this.selectedRows = val
        },
        // 新增
        handleAdd() {
            this.dialogTitle = '新增安全隐患排查'
            this.form = this.initFormData()
            this.dialogVisible = true
            this.$nextTick(() => {
                this.$refs.form.clearValidate()
            })
        },
        // 编辑
        handleEdit(row) {
            this.dialogTitle = '编辑安全隐患排查'
            this.form = { ...row }
            this.dialogVisible = true
            this.$nextTick(() => {
                this.$refs.form.clearValidate()
            })
        },
        // 删除
        handleDelete(row) {
            elConfirm('确定要删除此安全隐患排查记录吗？')
                .then(() => {
                    return api.del(row.id)
                })
                .then(() => {
                    elSuccess('删除成功')
                    this.fetchData()
                })
                .catch(() => {})
        },
        // 批量删除
        handleBatchDelete() {
            if (this.selectedRows.length === 0) {
                elError('请至少选择一条记录')
                return
            }
            
            const ids = this.selectedRows.map(row => row.id)
            
            elConfirm(`确定要删除选中的 ${ids.length} 条安全隐患排查记录吗？`)
                .then(() => {
                    return api.batchDelete(ids)
                })
                .then(() => {
                    elSuccess('批量删除成功')
                    this.fetchData()
                })
                .catch(() => {})
        },
        // 处理下拉菜单命令
        handleCommand(command, row) {
            switch (command) {
                case 'view':
                    this.viewDetail(row)
                    break
                case 'setAlert':
                    this.setAlert(row)
                    break
                case 'closeAlert':
                    this.closeAlert(row)
                    break
            }
        },
        // 查看详情
        viewDetail(row) {
            this.detailData = { ...row }
            this.detailVisible = true
        },
        // 设置预警
        setAlert(row) {
            api.setAlert(row.id)
                .then(() => {
                    elSuccess('预警设置成功')
                    this.fetchData()
                })
                .catch(() => {})
        },
        // 关闭预警
        closeAlert(row) {
            api.closeAlert(row.id)
                .then(() => {
                    elSuccess('预警关闭成功')
                    this.fetchData()
                })
                .catch(() => {})
        },
        // 导出
        handleExport() {
            api.exportInspection(this.searchForm)
                .then(response => {
                    download(response, `安全隐患排查_${new Date().toISOString().split('T')[0]}.xlsx`)
                })
                .catch(() => {})
        },
        // 提交表单
        submitForm() {
            this.$refs.form.validate(valid => {
                if (!valid) return
                
                const isAdd = !this.form.id
                const apiMethod = isAdd ? api.add : api.update
                
                apiMethod(this.form)
                    .then(() => {
                        elSuccess(isAdd ? '添加成功' : '更新成功')
                        this.dialogVisible = false
                        this.fetchData()
                    })
                    .catch(() => {})
            })
        },
        // 获取检查状态类型
        getCheckStatusType(status) {
            switch (status) {
                case '已过期':
                    return 'danger'
                case '即将过期':
                    return 'warning'
                case '正常':
                    return 'success'
                default:
                    return 'info'
            }
        },
        // 获取预警状态类型
        getAlertStatusType(status) {
            switch (status) {
                case '未设置':
                    return 'info'
                case '待预警':
                    return 'warning'
                case '已提醒':
                    return 'danger'
                case '已关闭':
                    return 'success'
                default:
                    return ''
            }
        },
        // 显示统计分析
        showStatistics() {
            this.statisticsVisible = true
            this.$nextTick(() => {
                this.initCharts()
            })
        },
        // 初始化图表
        initCharts() {
            api.getStatistics()
                .then(response => {
                    const data = response.data
                    
                    // 初始化隐患等级饼图
                    this.levelChart = window.echarts.init(this.$refs.levelChart, 'macarons')
                    this.levelChart.setOption({
                        title: {
                            text: '隐患等级分布',
                            left: 'center'
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: '{a} <br/>{b}: {c} ({d}%)'
                        },
                        legend: {
                            orient: 'vertical',
                            left: 'left',
                            data: ['一般隐患', '重大隐患']
                        },
                        series: [
                            {
                                name: '隐患等级',
                                type: 'pie',
                                radius: ['50%', '70%'],
                                avoidLabelOverlap: false,
                                label: {
                                    show: false,
                                    position: 'center'
                                },
                                emphasis: {
                                    label: {
                                        show: true,
                                        fontSize: '18',
                                        fontWeight: 'bold'
                                    }
                                },
                                labelLine: {
                                    show: false
                                },
                                data: data.levelStats.map(item => ({
                                    name: item.hazardLevel,
                                    value: item.count
                                }))
                            }
                        ]
                    })
                    
                    // 初始化整改状态饼图
                    this.resolveChart = window.echarts.init(this.$refs.resolveChart, 'macarons')
                    this.resolveChart.setOption({
                        title: {
                            text: '整改状态分布',
                            left: 'center'
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: '{a} <br/>{b}: {c} ({d}%)'
                        },
                        legend: {
                            orient: 'vertical',
                            left: 'left',
                            data: ['已整改', '未整改']
                        },
                        series: [
                            {
                                name: '整改状态',
                                type: 'pie',
                                radius: ['50%', '70%'],
                                avoidLabelOverlap: false,
                                label: {
                                    show: false,
                                    position: 'center'
                                },
                                emphasis: {
                                    label: {
                                        show: true,
                                        fontSize: '18',
                                        fontWeight: 'bold'
                                    }
                                },
                                labelLine: {
                                    show: false
                                },
                                data: [
                                    { name: '已整改', value: data.resolveStats.resolved },
                                    { name: '未整改', value: data.resolveStats.unresolved }
                                ]
                            }
                        ]
                    })
                    
                    // 更新即将到期隐患数量
                    this.urgentCount = data.urgentCount
                })
                .catch(() => {
                    this.$message.error('获取统计数据失败')
                })
        }
    }
}
</script>

<style scoped>
.app-container {
    padding: 20px;
}
.operation-buttons {
    margin-bottom: 15px;
}
.ellipsis {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 150px;
}
</style> 