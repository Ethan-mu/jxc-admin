<template>
  <div class="app-container doc-page-container">
    <el-card shadow="hover" style="height: 100%">
      <div slot="header">
        <el-form :inline="true" :model="search" size="mini" @submit.native.prevent>
          <!-- 搜索表单项 -->
          <el-form-item label="姓名">
            <el-input v-model.trim="search.name" placeholder="请输入姓名" clearable />
          </el-form-item>
          <el-form-item label="身份证号">
            <el-input v-model.trim="search.idNumber" placeholder="请输入身份证号" clearable />
          </el-form-item>
          <el-form-item label="工种">
            <el-input v-model.trim="search.currentJob" placeholder="请输入现工种" clearable />
          </el-form-item>
          <el-form-item label="公司级培训时间">
            <el-date-picker
              v-model="companyTrainDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd"
              style="width: 240px;"
            />
          </el-form-item>
          <!-- 按钮组 -->
          <el-form-item>
            <el-button-group>
              <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
              <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
              <el-button type="primary" icon="el-icon-download" @click="handleExport">导出</el-button>
            </el-button-group>
          </el-form-item>
        </el-form>
      </div>

      <!-- 表格 -->
      <el-table
        v-loading="loading"
        :data="list"
        border
        size="mini"
        :height="tableHeight"
        style="width: 100%"
      >
        <!-- 表格列 -->
        <el-table-column align="center" type="index" width="50" label="序号" />
        <el-table-column align="center" prop="name" label="姓名" min-width="100" />
        <el-table-column align="center" prop="gender" label="性别" width="60" />
        <el-table-column align="center" prop="age" label="年龄" width="60" />
        <el-table-column align="center" prop="phone" label="联系电话" min-width="120" />
        <el-table-column align="center" prop="originalJob" label="原工种" min-width="100" />
        <el-table-column align="center" prop="currentJob" label="现工种" min-width="100" />
        <el-table-column align="center" prop="idNumber" label="身份证号" min-width="180" />
        <el-table-column align="center" prop="companyTrainDate" label="公司级培训日期" min-width="140" />
        <el-table-column align="center" prop="workshopTrainDate" label="车间级培训日期" min-width="140" />
        <el-table-column align="center" prop="teamTrainDate" label="班组级培训日期" min-width="140" />
        
        <!-- 操作列 -->
        <el-table-column align="center" label="操作" width="150" fixed="right">
          <template slot-scope="{row}">
            <el-button-group>
              <el-button type="primary" size="mini" title="编辑" @click="handleEdit(row)">
                <i class="el-icon-edit" />
              </el-button>
              <el-button type="danger" size="mini" title="删除" @click="handleDelete(row)">
                <i class="el-icon-delete" />
              </el-button>
              <el-button type="info" size="mini" title="查看详情" @click="handleView(row)">
                <i class="el-icon-view" />
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-if="total > 0"
        :current-page.sync="page.current"
        :page-sizes="[10, 20, 50, 100]"
        :page-size.sync="page.size"
        :total="total"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="refresh"
        @size-change="refresh"
        style="margin-top: 15px; text-align: right"
      />

      <!-- 表单弹窗 -->
      <el-dialog
        :title="dialogType === 'add' ? '新增三级教育培训记录' : '编辑三级教育培训记录'"
        :visible.sync="dialogVisible"
        width="800px"
        @closed="closeDialog"
      >
        <el-form ref="form" :model="form" :rules="rules" label-width="120px" size="mini">
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本信息" name="basic">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="姓名" prop="name">
                    <el-input v-model.trim="form.name" placeholder="请输入姓名" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="性别" prop="gender">
                    <el-select v-model="form.gender" placeholder="请选择性别" style="width: 100%">
                      <el-option label="男" value="男" />
                      <el-option label="女" value="女" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="年龄" prop="age">
                    <el-input-number v-model="form.age" :min="18" :max="65" style="width: 100%" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="联系电话" prop="phone">
                    <el-input v-model.trim="form.phone" placeholder="请输入联系电话" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="原工种" prop="originalJob">
                    <el-input v-model.trim="form.originalJob" placeholder="请输入原工种" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="现工种" prop="currentJob">
                    <el-input v-model.trim="form.currentJob" placeholder="请输入现工种" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="入职日期" prop="joinDate">
                    <el-date-picker 
                      v-model="form.joinDate" 
                      type="date" 
                      placeholder="选择入职日期"
                      value-format="yyyy-MM-dd"
                      style="width: 100%" 
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="身份证号" prop="idNumber">
                    <el-input v-model.trim="form.idNumber" placeholder="请输入身份证号" />
                  </el-form-item>
                </el-col>
              </el-row>
            </el-tab-pane>
            
            <el-tab-pane label="公司级培训" name="company">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="培训时间" prop="companyTrainDate">
                    <el-date-picker
                      v-model="form.companyTrainDate"
                      type="date"
                      placeholder="选择培训时间"
                      value-format="yyyy-MM-dd"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="培训地点" prop="companyTrainLocation">
                    <el-input v-model.trim="form.companyTrainLocation" placeholder="请输入培训地点" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="培训主讲" prop="companyTrainLecturer">
                    <el-input v-model.trim="form.companyTrainLecturer" placeholder="请输入培训主讲人" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="培训成绩" prop="companyTrainScore">
                    <el-input v-model.trim="form.companyTrainScore" placeholder="请输入培训成绩" />
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item label="培训内容" prop="companyTrainContent">
                    <el-input
                      v-model.trim="form.companyTrainContent"
                      type="textarea"
                      :rows="3"
                      placeholder="请输入培训内容"
                    />
                  </el-form-item>
                </el-col>
              </el-row>
            </el-tab-pane>
            
            <el-tab-pane label="车间级培训" name="workshop">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="培训时间" prop="workshopTrainDate">
                    <el-date-picker
                      v-model="form.workshopTrainDate"
                      type="date"
                      placeholder="选择培训时间"
                      value-format="yyyy-MM-dd"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="培训地点" prop="workshopTrainLocation">
                    <el-input v-model.trim="form.workshopTrainLocation" placeholder="请输入培训地点" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="培训主讲" prop="workshopTrainLecturer">
                    <el-input v-model.trim="form.workshopTrainLecturer" placeholder="请输入培训主讲人" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="培训成绩" prop="workshopTrainScore">
                    <el-input v-model.trim="form.workshopTrainScore" placeholder="请输入培训成绩" />
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item label="培训内容" prop="workshopTrainContent">
                    <el-input
                      v-model.trim="form.workshopTrainContent"
                      type="textarea"
                      :rows="3"
                      placeholder="请输入培训内容"
                    />
                  </el-form-item>
                </el-col>
              </el-row>
            </el-tab-pane>
            
            <el-tab-pane label="班组级培训" name="team">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="培训时间" prop="teamTrainDate">
                    <el-date-picker
                      v-model="form.teamTrainDate"
                      type="date"
                      placeholder="选择培训时间"
                      value-format="yyyy-MM-dd"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="培训地点" prop="teamTrainLocation">
                    <el-input v-model.trim="form.teamTrainLocation" placeholder="请输入培训地点" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="培训主讲" prop="teamTrainLecturer">
                    <el-input v-model.trim="form.teamTrainLecturer" placeholder="请输入培训主讲人" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="培训成绩" prop="teamTrainScore">
                    <el-input v-model.trim="form.teamTrainScore" placeholder="请输入培训成绩" />
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item label="培训内容" prop="teamTrainContent">
                    <el-input
                      v-model.trim="form.teamTrainContent"
                      type="textarea"
                      :rows="3"
                      placeholder="请输入培训内容"
                    />
                  </el-form-item>
                </el-col>
              </el-row>
            </el-tab-pane>
          </el-tabs>
        </el-form>
        
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" :loading="submitting" @click="submitForm">确 定</el-button>
        </div>
      </el-dialog>
    </el-card>
  </div>
</template>

<script>
import { wic } from "@/util/auth"
import { elError, elSuccess, elConfirm } from "@/util/message"
import { search, add, update, del, exportTraining } from "@/api/doc/threeLevelTraining"

export default {
  name: "ThreeLevelTraining",

  data() {
    return {
      loading: false,
      submitting: false,
      tableHeight: 'calc(100vh - 280px)',
      
      // 搜索参数
      search: {
        name: '',
        idNumber: '',
        currentJob: ''
      },
      companyTrainDateRange: [],
      
      // 表格数据
      list: [],
      total: 0,
      page: {
        current: 1,
        size: 10
      },
      
      // 对话框控制
      dialogType: 'add',
      dialogVisible: false,
      activeTab: 'basic',
      
      // 表单数据
      form: {
        id: null,
        name: '',
        gender: '男',
        age: 30,
        phone: '',
        originalJob: '',
        currentJob: '',
        joinDate: '',
        idNumber: '',
        companyTrainDate: '',
        companyTrainLocation: '',
        companyTrainLecturer: '',
        companyTrainContent: '',
        companyTrainScore: '',
        workshopTrainDate: '',
        workshopTrainLocation: '',
        workshopTrainLecturer: '',
        workshopTrainContent: '',
        workshopTrainScore: '',
        teamTrainDate: '',
        teamTrainLocation: '',
        teamTrainLecturer: '',
        teamTrainContent: '',
        teamTrainScore: ''
      },
      
      // 表单验证规则
      rules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        gender: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        age: [
          { required: true, message: '请输入年龄', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入联系电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        currentJob: [
          { required: true, message: '请输入现工种', trigger: 'blur' }
        ],
        idNumber: [
          { required: true, message: '请输入身份证号', trigger: 'blur' },
          { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入正确的身份证号码', trigger: 'blur' }
        ],
        companyTrainDate: [
          { required: true, message: '请选择公司级培训时间', trigger: 'change' }
        ],
        companyTrainLocation: [
          { required: true, message: '请输入公司级培训地点', trigger: 'blur' }
        ],
        workshopTrainDate: [
          { required: true, message: '请选择车间级培训时间', trigger: 'change' }
        ],
        workshopTrainLocation: [
          { required: true, message: '请输入车间级培训地点', trigger: 'blur' }
        ],
        teamTrainDate: [
          { required: true, message: '请选择班组级培训时间', trigger: 'change' }
        ],
        teamTrainLocation: [
          { required: true, message: '请输入班组级培训地点', trigger: 'blur' }
        ]
      }
    }
  },

  computed: {
    // 权限控制
    ...wic({
      add: { request: () => Promise.resolve({}) },
      update: { request: () => Promise.resolve({}) },
      del: { request: () => Promise.resolve({}) }
    }),
    
    // 构建实际的搜索参数
    searchParams() {
      const params = {
        name: this.search.name,
        idNumber: this.search.idNumber,
        currentJob: this.search.currentJob,
        page: this.page.current,
        pageSize: this.page.size
      }
      
      if (this.companyTrainDateRange && this.companyTrainDateRange.length === 2) {
        params.companyTrainDateStart = this.companyTrainDateRange[0]
        params.companyTrainDateEnd = this.companyTrainDateRange[1]
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
          console.error('获取三级教育培训列表失败', err)
          elError('获取数据失败，请稍后重试')
        })
        .finally(() => {
          this.loading = false
        })
    },
    
    // 搜索
    handleSearch() {
      this.refresh(true)
    },
    
    // 新增
    handleAdd() {
      this.dialogType = 'add'
      this.dialogVisible = true
      this.activeTab = 'basic'
      this.form = {
        id: null,
        name: '',
        gender: '男',
        age: 30,
        phone: '',
        originalJob: '',
        currentJob: '',
        joinDate: '',
        idNumber: '',
        companyTrainDate: '',
        companyTrainLocation: '',
        companyTrainLecturer: '',
        companyTrainContent: '',
        companyTrainScore: '',
        workshopTrainDate: '',
        workshopTrainLocation: '',
        workshopTrainLecturer: '',
        workshopTrainContent: '',
        workshopTrainScore: '',
        teamTrainDate: '',
        teamTrainLocation: '',
        teamTrainLecturer: '',
        teamTrainContent: '',
        teamTrainScore: ''
      }
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    
    // 编辑
    handleEdit(row) {
      this.dialogType = 'edit'
      this.dialogVisible = true
      this.activeTab = 'basic'
      this.form = { ...row }
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    
    // 查看详情
    handleView(row) {
      this.$alert(`
        <div style="max-height: 500px; overflow-y: auto;">
          <h3 style="text-align: center;">${row.name}的三级教育培训详情</h3>
          <h4>基本信息</h4>
          <p><strong>姓名：</strong>${row.name}</p>
          <p><strong>性别：</strong>${row.gender}</p>
          <p><strong>年龄：</strong>${row.age}</p>
          <p><strong>联系电话：</strong>${row.phone}</p>
          <p><strong>原工种：</strong>${row.originalJob || '无'}</p>
          <p><strong>现工种：</strong>${row.currentJob}</p>
          <p><strong>入职日期：</strong>${row.joinDate}</p>
          <p><strong>身份证号：</strong>${row.idNumber}</p>
          
          <h4>公司级培训</h4>
          <p><strong>培训时间：</strong>${row.companyTrainDate}</p>
          <p><strong>培训地点：</strong>${row.companyTrainLocation}</p>
          <p><strong>培训主讲：</strong>${row.companyTrainLecturer}</p>
          <p><strong>培训内容：</strong>${row.companyTrainContent}</p>
          <p><strong>培训成绩：</strong>${row.companyTrainScore}</p>
          
          <h4>车间级培训</h4>
          <p><strong>培训时间：</strong>${row.workshopTrainDate}</p>
          <p><strong>培训地点：</strong>${row.workshopTrainLocation}</p>
          <p><strong>培训主讲：</strong>${row.workshopTrainLecturer}</p>
          <p><strong>培训内容：</strong>${row.workshopTrainContent}</p>
          <p><strong>培训成绩：</strong>${row.workshopTrainScore}</p>
          
          <h4>班组级培训</h4>
          <p><strong>培训时间：</strong>${row.teamTrainDate}</p>
          <p><strong>培训地点：</strong>${row.teamTrainLocation}</p>
          <p><strong>培训主讲：</strong>${row.teamTrainLecturer}</p>
          <p><strong>培训内容：</strong>${row.teamTrainContent}</p>
          <p><strong>培训成绩：</strong>${row.teamTrainScore}</p>
        </div>
      `, '三级教育培训详情', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '关闭',
        showClose: true
      })
    },
    
    // 删除
    handleDelete(row) {
      elConfirm(`确认删除 "${row.name}" 的三级教育培训记录吗？`, '删除确认')
        .then(() => {
          del(row.id)
            .then(res => {
              if (res.success) {
                elSuccess('删除成功')
                this.refresh()
              } else {
                elError(res.msg || '删除失败')
              }
            })
            .catch(err => {
              console.error('删除三级教育培训记录失败', err)
              elError('删除失败，请稍后重试')
            })
        })
    },
    
    // 导出
    handleExport() {
      exportTraining(this.searchParams)
        .then(res => {
          const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
          const link = document.createElement('a')
          link.href = URL.createObjectURL(blob)
          link.download = '三级教育培训记录.xlsx'
          link.click()
          URL.revokeObjectURL(link.href)
        })
        .catch(err => {
          console.error('导出三级教育培训记录失败', err)
          elError('导出失败，请稍后重试')
        })
    },
    
    // 关闭对话框
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
            console.error(this.dialogType === 'add' ? '添加三级教育培训记录失败' : '更新三级教育培训记录失败', err)
            elError(this.dialogType === 'add' ? '添加失败，请稍后重试' : '更新失败，请稍后重试')
          })
          .finally(() => {
            this.submitting = false
          })
      })
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  margin-top: 20px;
  text-align: right;
}
</style> 