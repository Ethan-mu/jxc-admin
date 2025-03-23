<template>
  <div class="app-container doc-page-container">
    <el-card shadow="hover" style="height: 100%">
      <div slot="header">
        <el-form :inline="true" :model="searchForm" size="mini" @submit.native.prevent>
          <!-- 搜索表单项 -->
          <el-form-item label="名称">
            <el-input v-model.trim="searchForm.name" placeholder="请输入名称" clearable/>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
              <el-option label="正常" value="normal"/>
              <el-option label="异常" value="error"/>
            </el-select>
          </el-form-item>
          <!-- 按钮组 -->
          <el-form-item>
            <el-button-group>
              <el-button type="primary" icon="el-icon-search" @click="search">查询</el-button>
              <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
              <el-button type="primary" icon="el-icon-download" @click="handleExport">导出</el-button>
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
      >
        <!-- 表格列 -->
        <el-table-column align="center" type="index" width="50"/>
        <el-table-column align="center" prop="name" label="名称" min-width="120"/>
        <el-table-column align="center" prop="status" label="状态" width="100">
          <template slot-scope="{row}">
            <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <!-- 操作列 -->
        <el-table-column align="center" label="操作" width="200" fixed="right">
          <template slot-scope="{row}">
            <el-button type="primary" size="mini" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="mini" @click="handleDelete(row)">删除</el-button>
            <el-dropdown size="mini" split-button type="primary" @command="command => handleCommand(command, row)">
              更多
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="view">查看详情</el-dropdown-item>
                <el-dropdown-item command="download">下载</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        :current-page.sync="page.current"
        :page-sizes="[10, 20, 50, 100]"
        :page-size.sync="page.size"
        :total="total"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
        style="margin-top: 15px; text-align: right"
      />

      <!-- 表单弹窗 -->
      <el-dialog
        :title="dialogType === 'add' ? '新增' : '编辑'"
        :visible.sync="dialogVisible"
        width="600px"
        @closed="closeDialog"
      >
        <el-form ref="form" :model="form" :rules="rules" label-width="100px" size="mini">
          <el-form-item label="名称" prop="name">
            <el-input v-model.trim="form.name" placeholder="请输入名称"/>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
              <el-option label="正常" value="normal"/>
              <el-option label="异常" value="error"/>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="submitting" @click="submitForm">确定</el-button>
        </div>
      </el-dialog>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "DocPageTemplate",
  data() {
    return {
      // 搜索表单
      searchForm: {
        name: '',
        status: '',
        page: 1,
        pageSize: 10
      },
      // 表格数据
      tableData: [],
      // 总数
      total: 0,
      // 分页
      page: {
        current: 1,
        size: 10
      },
      // 加载状态
      loading: false,
      // 提交状态
      submitting: false,
      // 对话框类型
      dialogType: 'add',
      // 对话框可见性
      dialogVisible: false,
      // 表单数据
      form: {
        id: null,
        name: '',
        status: 'normal'
      },
      // 表单验证规则
      rules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    // 获取数据
    fetchData() {
      this.loading = true
      // 模拟数据请求
      setTimeout(() => {
        this.tableData = [
          { id: 1, name: '示例数据1', status: 'normal' },
          { id: 2, name: '示例数据2', status: 'error' }
        ]
        this.total = 2
        this.loading = false
      }, 500)
    },
    // 搜索
    search() {
      this.page.current = 1
      this.fetchData()
    },
    // 处理页码变化
    handleCurrentChange() {
      this.fetchData()
    },
    // 处理页面大小变化
    handleSizeChange() {
      this.fetchData()
    },
    // 获取状态类型
    getStatusType(status) {
      const types = {
        normal: 'success',
        error: 'danger'
      }
      return types[status] || 'info'
    },
    // 新增
    handleAdd() {
      this.dialogType = 'add'
      this.form = { id: null, name: '', status: 'normal' }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    // 编辑
    handleEdit(row) {
      this.dialogType = 'edit'
      this.form = { ...row }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    // 删除
    handleDelete(row) {
      this.$confirm(`确认删除 "${row.name}" 吗？`, '提示', {
        type: 'warning'
      }).then(() => {
        // 模拟删除操作
        this.$message.success('删除成功')
        this.fetchData()
      }).catch(() => {})
    },
    // 处理命令
    handleCommand(command, row) {
      switch (command) {
        case 'view':
          this.$message.info(`查看详情：${row.name}`)
          break
        case 'download':
          this.$message.info(`下载：${row.name}`)
          break
      }
    },
    // 导出
    handleExport() {
      this.$message.info('导出功能')
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
        // 模拟提交
        setTimeout(() => {
          this.$message.success(this.dialogType === 'add' ? '添加成功' : '更新成功')
          this.dialogVisible = false
          this.fetchData()
          this.submitting = false
        }, 500)
      })
    }
  }
}
</script>

<style scoped>
/* 特殊样式可以在此添加 */
</style> 