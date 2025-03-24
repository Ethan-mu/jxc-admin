<template>
  <div class="doc-page-container">
    <el-card>
      <div class="search-form">
        <el-form :inline="true" :model="search" size="small">
          <el-form-item label="员工姓名">
            <el-input v-model="search.employeeName" placeholder="请输入员工姓名" clearable />
          </el-form-item>
          <el-form-item label="身份证号">
            <el-input v-model="search.idNumber" placeholder="请输入身份证号" clearable />
          </el-form-item>
          <el-form-item label="当前岗位">
            <el-input v-model="search.currentJob" placeholder="请输入当前岗位" clearable />
          </el-form-item>
          <el-form-item label="培训日期">
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd"
              @change="handleDateChange"
            />
          </el-form-item>
          <el-form-item>
            <el-button-group>
              <el-button type="primary" icon="el-icon-search" @click="queryData">查询</el-button>
              <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
              <el-button type="primary" icon="el-icon-download" @click="handleExport">导出</el-button>
            </el-button-group>
          </el-form-item>
        </el-form>
      </div>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        :height="tableHeight"
        style="width: 100%"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="employeeName" label="员工姓名" min-width="100" />
        <el-table-column prop="gender" label="性别" width="60" align="center" />
        <el-table-column prop="age" label="年龄" width="60" align="center" />
        <el-table-column prop="phone" label="联系电话" min-width="120" />
        <el-table-column prop="originalJob" label="原岗位" min-width="120" />
        <el-table-column prop="currentJob" label="现岗位" min-width="120" />
        <el-table-column prop="idNumber" label="身份证号" min-width="180" />
        <el-table-column prop="trainingDate" label="培训日期" min-width="110" />
        <el-table-column prop="trainingLocation" label="培训地点" min-width="120" />
        <el-table-column prop="lecturer" label="培训讲师" min-width="100" />
        <el-table-column prop="assessmentResult" label="考核成绩" min-width="90" align="center" />
        
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="scope">
            <el-button-group>
              <el-button
                size="mini"
                type="primary"
                icon="el-icon-edit"
                @click="handleEdit(scope.row)"
              />
              <el-button
                size="mini"
                type="danger"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
              />
              <el-dropdown trigger="click" @command="command => handleCommand(command, scope.row)">
                <el-button size="mini" type="primary" icon="el-icon-more" />
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="view">查看详情</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          background
          :current-page.sync="search.page"
          :page-sizes="[10, 20, 30, 50]"
          :page-size.sync="search.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 添加/编辑对话框 -->
    <el-dialog
      :title="dialogType === 'add' ? '添加继续教育培训记录' : '编辑继续教育培训记录'"
      :visible.sync="dialogVisible"
      width="60%"
      :close-on-click-modal="false"
      @closed="resetForm"
    >
      <el-tabs v-model="activeName">
        <el-tab-pane label="基本信息" name="basic">
          <el-form
            ref="form"
            :model="form"
            :rules="rules"
            label-width="100px"
            size="medium"
          >
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="员工姓名" prop="employeeName">
                  <el-input v-model="form.employeeName" placeholder="请输入员工姓名" />
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
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="年龄" prop="age">
                  <el-input-number v-model="form.age" :min="18" :max="65" style="width: 100%" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="联系电话" prop="phone">
                  <el-input v-model="form.phone" placeholder="请输入联系电话" />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="原岗位" prop="originalJob">
                  <el-input v-model="form.originalJob" placeholder="请输入原岗位" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="现岗位" prop="currentJob">
                  <el-input v-model="form.currentJob" placeholder="请输入现岗位" />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
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
                  <el-input v-model="form.idNumber" placeholder="请输入身份证号" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="培训信息" name="training">
          <el-form
            ref="formTraining"
            :model="form"
            :rules="rules"
            label-width="100px"
            size="medium"
          >
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="培训日期" prop="trainingDate">
                  <el-date-picker
                    v-model="form.trainingDate"
                    type="date"
                    placeholder="选择培训日期"
                    value-format="yyyy-MM-dd"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="培训地点" prop="trainingLocation">
                  <el-input v-model="form.trainingLocation" placeholder="请输入培训地点" />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="培训讲师" prop="lecturer">
                  <el-input v-model="form.lecturer" placeholder="请输入培训讲师" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="考核成绩" prop="assessmentResult">
                  <el-input-number
                    v-model="form.assessmentResult"
                    :precision="1"
                    :step="0.5"
                    :min="0"
                    :max="100"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-form-item label="课程内容" prop="courseContent">
              <el-input
                v-model="form.courseContent"
                type="textarea"
                :rows="4"
                placeholder="请输入课程内容"
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { search, add, update, del, exportEducation } from '@/api/training/continuingEducation'
import { timeFormat } from '@/util'

export default {
  name: 'ContinuingEducation',
  data() {
    const validateIdNumber = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入身份证号'))
      } else if (!/^\d{17}[\dXx]$/.test(value)) {
        callback(new Error('请输入正确的身份证号格式'))
      } else {
        callback()
      }
    }
    
    const validatePhone = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入联系电话'))
      } else if (!/^1[3-9]\d{9}$/.test(value)) {
        callback(new Error('请输入正确的手机号格式'))
      } else {
        callback()
      }
    }
    
    return {
      search: {
        page: 1,
        pageSize: 10,
        employeeName: '',
        idNumber: '',
        currentJob: '',
        trainingDateStart: '',
        trainingDateEnd: ''
      },
      dateRange: [],
      tableData: [],
      total: 0,
      loading: false,
      tableHeight: window.innerHeight - 300,
      
      // 对话框相关
      dialogVisible: false,
      dialogType: 'add', // add 或 edit
      activeName: 'basic',
      form: {
        id: null,
        employeeName: '',
        gender: '',
        age: 30,
        phone: '',
        originalJob: '',
        currentJob: '',
        joinDate: '',
        idNumber: '',
        trainingDate: '',
        trainingLocation: '',
        lecturer: '',
        courseContent: '',
        assessmentResult: 80
      },
      rules: {
        employeeName: [
          { required: true, message: '请输入员工姓名', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在2到20个字符之间', trigger: 'blur' }
        ],
        gender: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        age: [
          { required: true, message: '请输入年龄', trigger: 'blur' }
        ],
        phone: [
          { required: true, validator: validatePhone, trigger: 'blur' }
        ],
        originalJob: [
          { required: true, message: '请输入原岗位', trigger: 'blur' }
        ],
        currentJob: [
          { required: true, message: '请输入现岗位', trigger: 'blur' }
        ],
        joinDate: [
          { required: true, message: '请选择入职日期', trigger: 'change' }
        ],
        idNumber: [
          { required: true, validator: validateIdNumber, trigger: 'blur' }
        ],
        trainingDate: [
          { required: true, message: '请选择培训日期', trigger: 'change' }
        ],
        trainingLocation: [
          { required: true, message: '请输入培训地点', trigger: 'blur' }
        ],
        lecturer: [
          { required: true, message: '请输入培训讲师', trigger: 'blur' }
        ],
        assessmentResult: [
          { required: true, message: '请输入考核成绩', trigger: 'blur' }
        ],
        courseContent: [
          { required: true, message: '请输入课程内容', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    searchParams() {
      const params = {
        page: this.search.page,
        pageSize: this.search.pageSize,
        employeeName: this.search.employeeName,
        idNumber: this.search.idNumber,
        currentJob: this.search.currentJob
      }
      
      // 只有当日期不为空时才添加到请求参数中
      if (this.search.trainingDateStart) {
        params.trainingDateStart = this.search.trainingDateStart
      }
      
      if (this.search.trainingDateEnd) {
        params.trainingDateEnd = this.search.trainingDateEnd
      }
      
      return params
    }
  },
  created() {
    this.getList()
    // 监听窗口大小变化
    window.addEventListener('resize', this.resetTableHeight)
  },
  beforeDestroy() {
    // 移除事件监听
    window.removeEventListener('resize', this.resetTableHeight)
  },
  methods: {
    // 初始化表格高度
    resetTableHeight() {
      this.tableHeight = window.innerHeight - 300
    },
    
    // 处理日期范围变化
    handleDateChange(val) {
      if (val) {
        this.search.trainingDateStart = val[0]
        this.search.trainingDateEnd = val[1]
      } else {
        this.search.trainingDateStart = null
        this.search.trainingDateEnd = null
      }
    },
    
    // 获取列表数据
    getList() {
      this.loading = true
      search(this.searchParams).then(response => {
        if (response.success) {
          this.tableData = response.data.records
          this.total = response.data.total
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    
    // 查询按钮点击
    queryData() {
      this.search.page = 1
      this.getList()
    },
    
    // 处理页面大小变化
    handleSizeChange(val) {
      this.search.pageSize = val
      this.getList()
    },
    
    // 处理页码变化
    handleCurrentChange(val) {
      this.search.page = val
      this.getList()
    },
    
    // 添加按钮点击
    handleAdd() {
      this.dialogType = 'add'
      this.dialogVisible = true
      this.activeName = 'basic'
    },
    
    // 编辑按钮点击
    handleEdit(row) {
      this.dialogType = 'edit'
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
      this.activeName = 'basic'
    },
    
    // 删除按钮点击
    handleDelete(row) {
      this.$confirm('确认删除该培训记录吗？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        del(row.id).then(() => {
          this.$message.success('删除成功')
          this.getList()
        })
      }).catch(() => {})
    },
    
    // 处理下拉菜单命令
    handleCommand(command, row) {
      if (command === 'view') {
        this.viewDetails(row)
      }
    },
    
    // 查看详情
    viewDetails(row) {
      this.dialogType = 'view'
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
      this.activeName = 'basic'
      // 如果需要禁用表单，可以在此设置
    },
    
    // 导出功能
    handleExport() {
      this.$confirm('确认导出所有继续教育培训数据吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.download()
      }).catch(() => {})
    },
    
    // 下载文件
    download() {
      const loading = this.$loading({
        lock: true,
        text: '正在导出数据，请稍候...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      
      exportEducation(this.searchParams).then(response => {
        const blob = new Blob([response], { type: 'application/vnd.ms-excel' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = '继续教育培训数据_' + timeFormat(new Date(), 'yyyy-MM-dd') + '.xlsx'
        link.click()
        loading.close()
      }).catch(() => {
        loading.close()
      })
    },
    
    // 重置表单
    resetForm() {
      this.form = {
        id: null,
        employeeName: '',
        gender: '',
        age: 30,
        phone: '',
        originalJob: '',
        currentJob: '',
        joinDate: '',
        idNumber: '',
        trainingDate: '',
        trainingLocation: '',
        lecturer: '',
        courseContent: '',
        assessmentResult: 80
      }
      
      // 清除表单验证
      if (this.$refs.form) {
        this.$refs.form.clearValidate()
      }
      if (this.$refs.formTraining) {
        this.$refs.formTraining.clearValidate()
      }
    },
    
    // 提交表单
    submitForm() {
      // 同时验证两个表单
      const basicValid = new Promise((resolve) => {
        this.$refs.form.validate((valid) => {
          resolve(valid)
        })
      })
      
      const trainingValid = new Promise((resolve) => {
        this.$refs.formTraining.validate((valid) => {
          resolve(valid)
        })
      })
      
      Promise.all([basicValid, trainingValid]).then(results => {
        if (results.every(valid => valid)) {
          if (this.dialogType === 'add') {
            // 添加
            add(this.form).then(() => {
              this.$message.success('添加成功')
              this.dialogVisible = false
              this.getList()
            })
          } else {
            // 编辑
            update(this.form).then(() => {
              this.$message.success('更新成功')
              this.dialogVisible = false
              this.getList()
            })
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.doc-page-container {
  padding: 10px;
  
  .search-form {
    margin-bottom: 15px;
  }
  
  .pagination-container {
    margin-top: 15px;
    text-align: right;
  }
  
  .el-button-group {
    .el-button {
      margin-left: 0;
    }
  }
}
</style> 