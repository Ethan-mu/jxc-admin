<template>
    <el-dialog
        :title="dialogTitle"
        :visible.sync="visible"
        :close-on-click-modal="false"
        width="500px"
        @closed="$emit('input', false)"
    >
        <el-form ref="form" :model="form" :rules="rules" label-width="80px" size="small">
            <el-form-item label="上级部门" prop="pid">
                <el-cascader
                    v-model="form.pid"
                    :options="departmentOptions"
                    :props="{value: 'id', label: 'name', emitPath: false, checkStrictly: true}"
                    clearable
                    filterable
                    placeholder="不选择则默认为根部门"
                    style="width: 100%"
                    :disabled="type === 'see'"
                />
            </el-form-item>
            <el-form-item label="部门名称" prop="name">
                <el-input v-model="form.name" :disabled="type === 'see'" maxlength="20" show-word-limit />
            </el-form-item>
            <el-form-item label="部门描述" prop="describe">
                <el-input
                    v-model="form.describe"
                    :disabled="type === 'see'"
                    :rows="3"
                    maxlength="200"
                    show-word-limit
                    type="textarea"
                />
            </el-form-item>
            <el-form-item label="启用状态" prop="enable">
                <el-switch v-model="form.enable" :disabled="type === 'see'" />
            </el-form-item>
        </el-form>
        <div slot="footer">
            <el-button @click="visible = false">取 消</el-button>
            <el-button v-if="type !== 'see'" type="primary" @click="submit">确 定</el-button>
        </div>
    </el-dialog>
</template>

<script>
import { get } from "@/api/system/department"
import { add, update } from "@/api/system/department"
import { elSuccess } from "@/util/message"

export default {
    name: "EditDialog",
    
    props: {
        value: Boolean,
        data: Object,
        type: String
    },
    
    data() {
        return {
            visible: this.value,
            form: {
                id: null,
                pid: 1,
                name: '',
                describe: '',
                enable: true
            },
            rules: {
                name: [
                    { required: true, message: "请输入部门名称", trigger: "blur" },
                    { max: 20, message: "长度不能超过20个字符", trigger: "blur" }
                ]
            },
            departmentOptions: [],
            loading: false
        }
    },
    
    computed: {
        dialogTitle() {
            switch (this.type) {
                case 'add': return '添加部门'
                case 'edit': return '编辑部门'
                case 'see': return '查看部门'
                default: return '部门信息'
            }
        }
    },
    
    watch: {
        value(v) {
            this.visible = v
            if(v) {
                this.loadDepartments()
                this.resetForm()
            }
        },
        data(v) {
            if (v) {
                if (this.type === 'add') {
                    this.form.pid = v.id
                }
                else if (this.type === 'edit' || this.type === 'see') {
                    this.form = {
                        id: v.id,
                        pid: v.pid || 1,
                        name: v.name,
                        describe: v.describe,
                        enable: v.enable !== false
                    }
                }
            }
        }
    },
    
    methods: {
        resetForm() {
            this.$nextTick(() => {
                if (this.$refs.form) {
                    this.$refs.form.resetFields()
                    if (this.type === 'add') {
                        this.form = {
                            id: null,
                            pid: this.data ? this.data.id : 1,
                            name: '',
                            describe: '',
                            enable: true
                        }
                    }
                }
            })
        },
        
        async loadDepartments() {
            try {
                this.loading = true
                const { data } = await get.request(true)
                // 过滤掉当前部门及其子部门（编辑时不能选择自己或子部门作为父部门）
                if (this.type === 'edit' && this.data) {
                    this.departmentOptions = this.filterDepartments(data, this.data.id)
                } else {
                    this.departmentOptions = data
                }
            } catch (error) {
                console.error('加载部门数据失败', error)
            } finally {
                this.loading = false
            }
        },
        
        // 递归过滤掉当前节点及其子节点
        filterDepartments(departments, id) {
            return departments.filter(dept => {
                if (dept.id === id) return false
                if (dept.children && dept.children.length) {
                    dept.children = this.filterDepartments(dept.children, id)
                }
                return true
            })
        },
        
        submit() {
            this.$refs.form.validate(async valid => {
                if (!valid) return
                
                try {
                    this.loading = true
                    
                    const api = this.type === 'add' ? add : update
                    const { msg } = await api.request(this.form)
                    
                    elSuccess(msg || '操作成功')
                    this.visible = false
                    this.$emit('success')
                } catch (error) {
                    console.error('提交部门数据失败', error)
                } finally {
                    this.loading = false
                }
            })
        }
    }
}
</script>
