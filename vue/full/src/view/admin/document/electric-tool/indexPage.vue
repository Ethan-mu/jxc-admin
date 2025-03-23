<template>
  <el-card shadow="hover" style="height: 100%">
    <div slot="header">
      <el-form ref="searchForm" :inline="true" :model="search" size="mini">
        <el-form-item label="证书名称">
          <el-input v-model.trim="search.certificateName" clearable placeholder="请输入证书名称"/>
        </el-form-item>
        <el-form-item label="工具类型">
          <el-input v-model.trim="search.toolType" clearable placeholder="请输入工具类型"/>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="search.status" clearable placeholder="请选择状态">
            <el-option label="正常" value="正常"/>
            <el-option label="损坏" value="损坏"/>
            <el-option label="待更换" value="待更换"/>
          </el-select>
        </el-form-item>
        <el-form-item label="检查状态">
          <el-select v-model="search.checkStatus" clearable placeholder="请选择检查状态">
            <el-option label="待检查" value="待检查"/>
            <el-option label="合格" value="合格"/>
            <el-option label="不合格" value="不合格"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button-group>
            <el-button type="primary" @click="search.page = 1;refresh()">查询</el-button>
            <el-button type="primary" @click="add" v-permission="'doc:electricTool:add'">添加</el-button>
            <el-button type="primary" @click="exportData" v-permission="'doc:electricTool:export'">导出</el-button>
          </el-button-group>
        </el-form-item>
      </el-form>
    </div>

    <el-table v-loading="loading" :data="data" border size="mini" height="calc(100% - 70px)" style="width: 100%">
      <el-table-column align="center" label="ID" prop="id" width="80"/>
      <el-table-column align="center" label="证书名称" prop="certificateName"/>
      <el-table-column align="center" label="工具类型" prop="toolType"/>
      <el-table-column align="center" label="规格" prop="specifications" width="120"/>
      <el-table-column align="center" label="出厂编号" prop="factoryNo" width="120"/>
      <el-table-column align="center" label="制造厂商" prop="manufacturer"/>
      <el-table-column align="center" label="使用状态" width="100">
        <template slot-scope="{row}">
          <span :style="{color: getStatusColor(row.status)}">{{row.status}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="使用日期">
        <template slot-scope="{row}">
          {{row.useDate}}
        </template>
      </el-table-column>
      <el-table-column align="center" label="复检日期">
        <template slot-scope="{row}">
          {{row.recheckDate}}
        </template>
      </el-table-column>
      <el-table-column align="center" label="检查状态" width="100">
        <template slot-scope="{row}">
          <span :style="{color: getCheckStatusColor(row.checkStatus)}">{{row.checkStatus}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="剩余天数">
        <template slot-scope="{row}">
          <span :style="{color: row.remainingDays >= 0 ? '' : 'red'}">
            {{row.remainingDays >= 0 ? row.remainingDays + '天' : '已超期' + Math.abs(row.remainingDays) + '天'}}
          </span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="设备管理人员" prop="managerName"/>
      <el-table-column align="center" label="管理人员联系方式" prop="managerContact" width="150"/>
      <el-table-column align="center" label="主管领导" prop="leaderName"/>
      <el-table-column align="center" label="领导联系方式" prop="leaderContact" width="150"/>
      <el-table-column align="center" label="存放位置" prop="location"/>
      <el-table-column align="center" label="预警状态" width="100">
        <template slot-scope="{row}">
          <span :style="{color: getAlertStatusColor(row.alertStatus)}">
            {{row.alertStatus}}
          </span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="预警次数" prop="alertCount" width="80"/>
      <el-table-column align="center" label="操作" width="280">
        <template slot-scope="{row, $index}">
          <el-button type="primary" size="mini" @click="edit(row)" v-permission="'doc:electricTool:update'">编辑</el-button>
          <el-button type="danger" size="mini" @click="del(row.id)" v-permission="'doc:electricTool:del'">删除</el-button>
          <el-button type="warning" size="mini" @click="setAlert(row.id)" v-if="row.alertStatus === '未预警'" v-permission="'doc:electricTool:alert'">
            设置预警
          </el-button>
          <el-button type="success" size="mini" @click="closeAlert(row.id)" v-if="row.alertStatus !== '未预警'" v-permission="'doc:electricTool:alert'">
            关闭预警
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      :current-page.sync="search.page"
      :page-sizes="[10, 20, 50, 100]"
      :page-size.sync="search.pageSize"
      :total="total"
      background
      layout="total, sizes, prev, pager, next, jumper"
      @current-change="refresh"
      @size-change="refresh"
      style="margin-top: 10px; text-align: right"
    />

    <el-dialog :title="form.id ? '编辑' : '添加'" :visible.sync="visible" width="700px">
      <el-form ref="form" :model="form" :rules="rules" label-width="130px" size="mini">
        <el-form-item label="证书名称" prop="certificateName">
          <el-input v-model.trim="form.certificateName" placeholder="请输入证书名称"/>
        </el-form-item>
        <el-form-item label="工具类型" prop="toolType">
          <el-input v-model.trim="form.toolType" placeholder="请输入工具类型"/>
        </el-form-item>
        <el-form-item label="规格" prop="specifications">
          <el-input v-model.trim="form.specifications" placeholder="请输入规格"/>
        </el-form-item>
        <el-form-item label="出厂编号" prop="factoryNo">
          <el-input v-model.trim="form.factoryNo" placeholder="请输入出厂编号"/>
        </el-form-item>
        <el-form-item label="制造厂商" prop="manufacturer">
          <el-input v-model.trim="form.manufacturer" placeholder="请输入制造厂商"/>
        </el-form-item>
        <el-form-item label="使用状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="正常" value="正常"/>
            <el-option label="损坏" value="损坏"/>
            <el-option label="待更换" value="待更换"/>
          </el-select>
        </el-form-item>
        <el-form-item label="存放位置" prop="location">
          <el-input v-model.trim="form.location" placeholder="请输入存放位置"/>
        </el-form-item>
        <el-form-item label="设备管理人员" prop="managerName">
          <el-input v-model.trim="form.managerName" placeholder="请输入设备管理人员"/>
        </el-form-item>
        <el-form-item label="管理人员联系方式" prop="managerContact">
          <el-input v-model.trim="form.managerContact" placeholder="请输入管理人员联系方式"/>
        </el-form-item>
        <el-form-item label="主管领导" prop="leaderName">
          <el-input v-model.trim="form.leaderName" placeholder="请输入主管领导"/>
        </el-form-item>
        <el-form-item label="领导联系方式" prop="leaderContact">
          <el-input v-model.trim="form.leaderContact" placeholder="请输入领导联系方式"/>
        </el-form-item>
        <el-form-item label="使用日期" prop="useDate">
          <el-date-picker v-model="form.useDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd"/>
        </el-form-item>
        <el-form-item label="复检日期" prop="recheckDate">
          <el-date-picker v-model="form.recheckDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd"/>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="submit">确定</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import electricToolApi from '@/api/doc/electricTool'
import { mapGetters } from 'vuex'

export default {
  name: 'electricTool',
  data() {
    return {
      loading: false,
      data: [],
      total: 0,
      search: {
        page: 1,
        pageSize: 10,
        certificateName: '',
        toolType: '',
        status: '',
        checkStatus: ''
      },
      visible: false,
      form: this.initForm(),
      rules: {
        certificateName: [{ required: false, message: '请输入证书名称', trigger: 'blur' }],
        toolType: [{ required: true, message: '请输入工具类型', trigger: 'blur' }],
        specifications: [{ required: false, message: '请输入规格', trigger: 'blur' }],
        factoryNo: [{ required: false, message: '请输入出厂编号', trigger: 'blur' }],
        manufacturer: [{ required: false, message: '请输入制造厂商', trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }],
        location: [{ required: true, message: '请输入存放位置', trigger: 'blur' }],
        managerName: [{ required: false, message: '请输入设备管理人员', trigger: 'blur' }],
        managerContact: [{ required: false, message: '请输入管理人员联系方式', trigger: 'blur' }],
        leaderName: [{ required: false, message: '请输入主管领导', trigger: 'blur' }],
        leaderContact: [{ required: false, message: '请输入领导联系方式', trigger: 'blur' }],
        useDate: [{ required: true, message: '请选择使用日期', trigger: 'change' }],
        recheckDate: [{ required: true, message: '请选择复检日期', trigger: 'change' }]
      }
    }
  },
  
  computed: {
    ...mapGetters(['permission'])
  },
  
  created() {
    this.refresh()
  },
  
  methods: {
    refresh() {
      this.loading = true
      electricToolApi.search(this.search).then(({ data }) => {
        this.data = data.records
        this.total = data.total
      }).finally(() => {
        this.loading = false
      })
    },
    
    add() {
      this.form = this.initForm()
      this.visible = true
      this.$nextTick(() => {
        this.$refs.form.clearValidate()
      })
    },
    
    edit(row) {
      this.form = { ...row }
      this.visible = true
      this.$nextTick(() => {
        this.$refs.form.clearValidate()
      })
    },
    
    del(id) {
      this.$confirm('确认删除该电动防护工器具?', '提示', {
        type: 'warning'
      }).then(() => {
        electricToolApi.del(id).then(({ msg }) => {
          this.$message.success(msg)
          this.refresh()
        })
      }).catch(() => {})
    },
    
    setAlert(id) {
      this.$confirm('确认为该电动防护工器具设置预警?', '提示', {
        type: 'warning'
      }).then(() => {
        electricToolApi.setAlert(id).then(({ msg }) => {
          this.$message.success(msg)
          this.refresh()
        })
      }).catch(() => {})
    },
    
    closeAlert(id) {
      this.$confirm('确认关闭该电动防护工器具的预警?', '提示', {
        type: 'warning'
      }).then(() => {
        electricToolApi.closeAlert(id).then(({ msg }) => {
          this.$message.success(msg)
          this.refresh()
        })
      }).catch(() => {})
    },
    
    exportData() {
      electricToolApi.exportTool(this.search)
    },
    
    submit() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const isAdd = !this.form.id
        const api = isAdd ? electricToolApi.add : electricToolApi.update
        
        api(this.form).then(({ msg }) => {
          this.$message.success(msg)
          this.visible = false
          this.refresh()
        })
      })
    },
    
    initForm() {
      return {
        id: null,
        certificateName: '',
        toolType: '',
        specifications: '',
        factoryNo: '',
        manufacturer: '',
        status: '正常',
        location: '',
        managerName: '',
        managerContact: '',
        leaderName: '',
        leaderContact: '',
        useDate: '',
        recheckDate: '',
        alertStatus: '未预警',
        alertCount: 0
      }
    },
    
    getStatusColor(status) {
      switch (status) {
        case '正常': return 'green'
        case '损坏': return 'red'
        case '待更换': return 'orange'
        default: return ''
      }
    },
    
    getCheckStatusColor(status) {
      switch (status) {
        case '待检查': return 'blue'
        case '合格': return 'green'
        case '不合格': return 'red'
        default: return ''
      }
    },
    
    getAlertStatusColor(status) {
      switch (status) {
        case '未预警': return ''
        case '待预警': return 'orange'
        case '已提醒': return 'green'
        default: return ''
      }
    }
  }
}
</script> 