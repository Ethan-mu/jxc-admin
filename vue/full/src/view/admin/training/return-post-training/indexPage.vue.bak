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
          <el-form-item label="培训级别">
            <el-select v-model="search.trainingLevel" placeholder="请选择培训级别" clearable style="width: 120px;">
              <el-option label="公司级" value="公司级" />
              <el-option label="车间级" value="车间级" />
              <el-option label="班组级" value="班组级" />
            </el-select>
          </el-form-item>
          <el-form-item label="培训时间">
            <el-date-picker
              v-model="trainingDateRange"
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
        <el-table-column align="center" prop="originalPost" label="原岗位" min-width="100" />
        <el-table-column align="center" prop="currentPost" label="现岗位" min-width="100" />
        <el-table-column align="center" prop="trainingLevel" label="培训级别" min-width="100" />
        <el-table-column align="center" prop="trainingDate" label="培训时间" min-width="120" />
        <el-table-column align="center" prop="trainingLocation" label="培训地点" min-width="120" />
        <el-table-column align="center" prop="lecturer" label="授课人" min-width="100" />
        <el-table-column align="center" prop="score" label="考核成绩" min-width="100" />
        
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
        :title="dialogType === 'add' ? '新增复岗教育培训记录' : '编辑复岗教育培训记录'"
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
                  <el-form-item label="身份证号" prop="idNumber">
                    <el-input v-model.trim="form.idNumber" placeholder="请输入身份证号" />
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
              </el-row>
            </el-tab-pane>
            
            <el-tab-pane label="岗位信息" name="post">
              <el-row :gutter="20">
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
                  <el-form-item label="原岗位" prop="originalPost">
                    <el-input v-model.trim="form.originalPost" placeholder="请输入原岗位" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="现岗位" prop="currentPost">
                    <el-input v-model.trim="form.currentPost" placeholder="请输入现岗位" />
                  </el-form-item>
                </el-col>
              </el-row>
            </el-tab-pane>
            
            <el-tab-pane label="培训信息" name="training">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="培训级别" prop="trainingLevel">
                    <el-select v-model="form.trainingLevel" placeholder="请选择培训级别" style="width: 100%">
                      <el-option label="公司级" value="公司级" />
                      <el-option label="车间级" value="车间级" />
                      <el-option label="班组级" value="班组级" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="培训时间" prop="trainingDate">
                    <el-date-picker
                      v-model="form.trainingDate"
                      type="date"
                      placeholder="选择培训时间"
                      value-format="yyyy-MM-dd"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="培训地点" prop="trainingLocation">
                    <el-input v-model.trim="form.trainingLocation" placeholder="请输入培训地点" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="授课人" prop="lecturer">
                    <el-input v-model.trim="form.lecturer" placeholder="请输入授课人" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="考核成绩" prop="score">
                    <el-input v-model.trim="form.score" placeholder="请输入考核成绩" />
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item label="主讲内容" prop="mainContent">
                    <el-input
                      v-model.trim="form.mainContent"
                      type="textarea"
                      :rows="4"
                      placeholder="请输入主讲内容"
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
import { search, add, update, del, exportTraining } from "@/api/training/returnPostTraining"

export default {
  name: "ReturnPostTraining",

  data() {
    return {
      loading: false,
      submitting: false,
      tableHeight: 'calc(100vh - 280px)',
      
      // 搜索参数
      search: {
        name: '',
        idNumber: '',
        currentJob: '',
        trainingLevel: ''
      },
      trainingDateRange: [],
      
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
        originalPost: '',
        currentPost: '',
        trainingLevel: '公司级',
        trainingDate: '',
        trainingLocation: '',
        lecturer: '',
        mainContent: '',
        score: ''
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
        idNumber: [
          { required: true, message: '请输入身份证号', trigger: 'blur' },
          { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入正确的身份证号码', trigger: 'blur' }
        ],
        currentJob: [
          { required: true, message: '请输入现工种', trigger: 'blur' }
        ],
        currentPost: [
          { required: true, message: '请输入现岗位', trigger: 'blur' }
        ],
        trainingLevel: [
          { required: true, message: '请选择培训级别', trigger: 'change' }
        ],
        trainingDate: [
          { required: true, message: '请选择培训时间', trigger: 'change' }
        ],
        trainingLocation: [
          { required: true, message: '请输入培训地点', trigger: 'blur' }
        ],
        lecturer: [
          { required: true, message: '请输入授课人', trigger: 'blur' }
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
        trainingLevel: this.search.trainingLevel,
        page: this.page.current,
        pageSize: this.page.size
      }
      
      if (this.trainingDateRange && this.trainingDateRange.length === 2) {
        params.trainingDateStart = this.trainingDateRange[0]
        params.trainingDateEnd = this.trainingDateRange[1]
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
          console.error('获取复岗教育培训列表失败', err)
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
        originalPost: '',
        currentPost: '',
        trainingLevel: '公司级',
        trainingDate: '',
        trainingLocation: '',
        lecturer: '',
        mainContent: '',
        score: ''
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
          <h3 style="text-align: center;">${row.name}的复岗教育培训详情</h3>
          <h4>基本信息</h4>
          <p><strong>姓名：</strong>${row.name}</p>
          <p><strong>性别：</strong>${row.gender}</p>
          <p><strong>年龄：</strong>${row.age}</p>
          <p><strong>联系电话：</strong>${row.phone}</p>
          <p><strong>身份证号：</strong>${row.idNumber}</p>
          <p><strong>入职日期：</strong>${row.joinDate}</p>
          
          <h4>岗位信息</h4>
          <p><strong>原工种：</strong>${row.originalJob || '无'}</p>
          <p><strong>现工种：</strong>${row.currentJob}</p>
          <p><strong>原岗位：</strong>${row.originalPost || '无'}</p>
          <p><strong>现岗位：</strong>${row.currentPost}</p>
          
          <h4>培训信息</h4>
          <p><strong>培训级别：</strong>${row.trainingLevel}</p>
          <p><strong>培训时间：</strong>${row.trainingDate}</p>
          <p><strong>培训地点：</strong>${row.trainingLocation}</p>
          <p><strong>授课人：</strong>${row.lecturer}</p>
          <p><strong>主讲内容：</strong>${row.mainContent || '无'}</p>
          <p><strong>考核成绩：</strong>${row.score || '无'}</p>
        </div>
      `, '复岗教育培训详情', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '关闭',
        showClose: true
      })
    },
    
    // 删除
    handleDelete(row) {
      elConfirm(`确认删除 "${row.name}" 的复岗教育培训记录吗？`, '删除确认')
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
              console.error('删除复岗教育培训记录失败', err)
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
          link.download = '复岗教育培训记录.xlsx'
          link.click()
          URL.revokeObjectURL(link.href)
        })
        .catch(err => {
          console.error('导出复岗教育培训记录失败', err)
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
            console.error(this.dialogType === 'add' ? '添加复岗教育培训记录失败' : '更新复岗教育培训记录失败', err)
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