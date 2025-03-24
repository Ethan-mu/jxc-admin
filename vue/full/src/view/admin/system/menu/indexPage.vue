<template>
    <el-card>
        <div v-if="showTip" class="tip-row">线上版本没有开放动态路由功能</div>

        <el-row class="button-group">
            <template v-if="canAdd">
                <el-button icon="el-icon-plus" size="small" type="primary" @click="addRoot">添加根节点</el-button>
                <el-button icon="el-icon-plus" size="small" type="primary" @click="addChild">添加子节点</el-button>
            </template>

            <el-button v-if="canDel" icon="el-icon-delete" size="small" type="danger" @click="del">删 除</el-button>

            <el-dropdown placement="bottom" size="small" @command="more">
                <el-button plain size="small">
                    更多操作
                    <i class="el-icon-arrow-down el-icon--right"/>
                </el-button>

                <template v-slot:dropdown>
                    <el-dropdown-menu>
                        <el-dropdown-item :command="{action:'expand',level:0}">展开全部</el-dropdown-item>
                        <el-dropdown-item :command="{action:'collapse',level:0}">收起全部</el-dropdown-item>
                        <el-dropdown-item :command="{action:'expand',level:1}">仅展开一级</el-dropdown-item>
                    </el-dropdown-menu>
                </template>
            </el-dropdown>
        </el-row>

        <el-row :gutter="100" style="margin-top: 20px;">
            <el-col :xs="24" :sm="11" :md="9" :lg="8" :xl="6">
                <el-alert show-icon :closable="false">
                    <template v-slot:title>
                        当前选择编辑：
                        <el-tag v-if="curNode" size="mini" closable @close="curNode = null">
                            {{ getNodeTitle(curNode) }}
                        </el-tag>
                    </template>
                </el-alert>

                <el-input
                    v-model="search"
                    placeholder="输入菜单名搜索"
                    suffix-icon="el-icon-search"
                    size="small"
                    style="margin-top: 10px"
                    @input="v => $refs.tree.filter(v)"
                />

                <div style="max-height: 660px;overflow: auto;margin-top: 10px">
                    <el-tree
                        ref="tree"
                        :data="menus"
                        node-key="id"
                        highlight-current
                        accordion
                        :expand-on-click-node="false"
                        :filter-node-method="filterNodeMethod"
                        @node-click="nodeClick"
                    >
                        <span slot-scope="{node}" class="el-tree-node__label">
                            <v-icon :icon="getNodeInfo(node).icon"/>
                            {{ getNodeTitle(node) }}
                        </span>
                    </el-tree>
                </div>
            </el-col>

            <el-col :xs="24" :sm="13" :md="15" :lg="13" :xl="11">
                <abstract-form 
                    ref="form"
                    v-if="showForm" 
                    :model="form" 
                    :rules="rules"
                    label-width="100px"
                >
                    <el-form-item label="类型">
                        <el-select
                            v-if="showTypeSelector"
                            :value="form.type"
                            placeholder="请选择类型"
                            @change="typeChange"
                        >
                            <template v-slot:prefix>
                                <v-icon :icon="getNodeInfo({data:form}).icon"/>
                            </template>

                            <el-option
                                v-for="{label,value,icon} in nodeType"
                                :key="value"
                                :label="label"
                                :value="value"
                                :disabled="value === 3"
                            >
                                <v-icon :icon="icon"/>
                                {{ label }}
                            </el-option>
                        </el-select>

                        <template v-else>
                            <v-icon :icon="getNodeInfo({data:form}).icon"/>
                            {{ getNodeInfo({data: form}).label }}
                        </template>
                    </el-form-item>

                    <el-form-item v-if="form.pid" label="上级菜单">
                        <v-icon :icon="getParentNodeInfo().icon"/>
                        {{ getParentNodeInfo().title }}
                    </el-form-item>

                    <el-form-item 
                        v-if="form.type !== 3" 
                        label="标题" 
                        prop="meta.title"
                    >
                        <el-input v-model="form.meta.title" placeholder="请输入标题"/>
                    </el-form-item>

                    <el-form-item v-else label="名称" prop="name">
                        <el-input v-model="form.name" placeholder="请输入名称"/>
                    </el-form-item>

                    <el-form-item label="路径" prop="path">
                        <el-input v-model="form.path" placeholder="请输入路径"/>
                    </el-form-item>

                    <el-form-item v-if="form.type === 2" label="组件">
                        <el-input v-model="form.component" placeholder="请输入组件路径"/>
                    </el-form-item>

                    <el-form-item v-if="form.type !== 3" label="图标">
                        <el-input v-model="form.meta.icon" placeholder="请输入图标名称">
                            <template v-slot:prepend>
                                <v-icon v-if="form.meta.icon" :icon="form.meta.icon"/>
                                <i v-else>无</i>
                            </template>
                        </el-input>
                    </el-form-item>

                    <el-form-item v-if="form.type !== 3" label="排序">
                        <el-input-number v-model="form.meta.sort" :min="0" :step="1" style="width: 140px"/>
                    </el-form-item>

                    <el-form-item v-if="form.type === 2 && form.component" label="路由名称">
                        <el-input v-model="form.name" placeholder="请输入路由名称"/>
                    </el-form-item>

                    <el-form-item v-if="form.type !== 3" label="Meta配置">
                        <json-editor 
                            v-model="form.metaStr" 
                            @change="metaChange" 
                            @input="metaChange"
                        />
                    </el-form-item>

                    <el-form-item label="专属管理员">
                        <el-switch v-model="form.admin"/>
                    </el-form-item>

                    <el-form-item label="启用">
                        <el-switch v-model="form.enable"/>
                    </el-form-item>

                    <el-form-item>
                        <el-button
                            v-if="showSaveBtn"
                            type="primary"
                            :loading="loading"
                            @click="save"
                        >
                            保存
                        </el-button>
                        <el-button @click="curNode = null">取消</el-button>
                    </el-form-item>
                </abstract-form>
            </el-col>
        </el-row>
    </el-card>
</template>

<script>
import treePageMixin from '@/mixin/treePageMixin'
import AbstractForm from "@/component/abstract/Form"
import JsonEditor from "@/component/editor/JsonEditor"
import {add, update, del} from '@/api/system/resource'
import {isEmpty, mergeObj, resetObj} from "@/util"
import {wic} from "@/util/auth"
import {expandControl} from "@/util/element-ui/elTree"
import {elSuccess, elError, elConfirm} from "@/util/message"
import {nodeType, getNodeInfo, getNodeTitle} from "./common"

export default {
    name: "menuManagement",

    mixins: [treePageMixin],

    components: {AbstractForm, JsonEditor},

    data() {
        const validateTitle = (rule, value, callback) => {
            if (this.form && this.form.type !== 3 && (!this.form.meta || isEmpty(this.form.meta.title))) {
                callback(new Error('标题不能为空'))
            } else {
                callback()
            }
        }
        const validateName = (rule, value, callback) => {
            if (isEmpty(value)) {
                callback(new Error('名称不能为空'))
            } else {
                callback()
            }
        }
        return {
            showTip: process.env.NODE_ENV !== 'development',

            loading: false,
            search: '',
            curNode: null,
            type: 'edit', //标记是添加(add)还是编辑(edit)操作
            nodeType,
            form: {
                id: null,
                pid: null,
                name: '',
                type: null,
                path: '',
                component: '',
                meta: {
                    title: '',
                    icon: '',
                    sort: 0,
                    hidden: false,
                    activeMenu: '',
                    noAuth: false,
                    iframe: '',
                    usePathKey: false,
                    useFullPathKey: false
                },
                metaStr: '',
                admin: false,
                enable: true
            },
            rules: {
                'meta.title': [{validator: validateTitle, trigger: 'blur'}],
                'name': [{validator: validateName, trigger: 'blur'}],
                'path': [{required: true, message: '路径不能为空', trigger: 'blur'}],
            }
        }
    },

    computed: {
        ...wic({add, update, del}),

        menus() {
            return this.$store.state.resource.resourceTree
        },
        showForm() {
            return this.type === 'add' && this.form.type === 0 || this.curNode
        },
        showTypeSelector() {
            //0.根节点的类型不能被改变
            const isRoot = this.form.type === 0
            //1.非数据接口
            const noApiNode = this.form.type !== 3
            //2.其下无子节点
            const noChild = this.type === 'add' && isRoot
                || this.type === 'edit' && this.curNode.childNodes.length <= 0
            //3.父节点只能为顶部或聚合菜单
            const parentIsFolderMenu =
                isRoot
                    ? true
                    : this.type === 'add' && [0, 1].includes(this.curNode.data.type)
                    || this.type === 'edit' && [0, 1].includes(this.curNode.parent.data.type)
            return !isRoot && noApiNode && noChild && parentIsFolderMenu
        },
        showSaveBtn() {
            return this.type === 'add' && this.canAdd || this.type === 'edit' && this.canUpdate
        }
    },

    created() {
        this.init()
    },

    methods: {
        getNodeInfo,
        getNodeTitle,
        getParentNodeInfo() {
            const node = this.type === 'add' ? this.curNode : this.curNode.parent
            const {icon} = getNodeInfo(node)
            return {icon, title: getNodeTitle(node)}
        },
        filterNodeMethod(value, data) {
            if (isEmpty(value)) return true
            return getNodeTitle({data}).includes(value)
        },
        nodeClick(data, node) {
            if (this.curNode === node) {
                this.$refs.tree.setCurrentKey()
                this.curNode = null
                this.clearForm()
                return
            }
            
            this.curNode = node
            this.afterNodeClick(data, node, this.$refs.form)
        },
        afterNodeClick(data, node, ref, clear) {
            if (clear) {
                this.clearForm()
                return
            }
            
            this.type = 'edit'
            this.$refs.form && this.$refs.form.clearValidate()
            
            // 深度复制对象，避免直接引用
            this.form.id = data.id
            this.form.pid = data.pid
            this.form.name = data.name || ''
            this.form.type = data.type
            this.form.path = data.path || ''
            this.form.component = data.component || ''
            this.form.admin = !!data.admin
            this.form.enable = data.enable !== false
            
            // 处理meta数据
            resetObj(this.form.meta)
            if (data.meta) {
                mergeObj(this.form.meta, data.meta)
                this.form.metaStr = JSON.stringify(data.meta, null, 2)
            } else {
                this.form.metaStr = JSON.stringify({
                    title: this.form.meta.title || '',
                    icon: this.form.meta.icon || '',
                    sort: this.form.meta.sort || 0
                }, null, 2)
            }
        },
        //json编辑器值改变时
        metaChange(v) {
            try {
                v = JSON.parse(v)
                resetObj(this.form.meta)
                mergeObj(this.form.meta, v)
            }
            catch (e) {
                console.error('JSON解析错误:', e)
                return
            }
        },
        //表单中菜单类型选择器改变时
        typeChange(v) {
            //从顶部或聚合改为页面菜单时，清空component
            if (v === 2 && [0, 1].includes(this.form.type)) {
                this.form.component = null
            }
            this.form.type = v
        },

        addRoot() {
            if (this.loading) return
            this.curNode = null
            this.type = 'add'
            this.clearForm()
            this.form.pid = 0
            this.form.type = 0
            
            // 确保meta对象正确初始化
            if (!this.form.meta) {
                this.form.meta = {
                    title: '',
                    icon: '',
                    sort: 0
                }
            }
            
            this.form.metaStr = JSON.stringify(this.form.meta, null, 2)
        },
        addChild() {
            if (this.loading) return
            if (!this.curNode) return elError('请选择一个节点')
            const {data: {id, type}} = this.curNode
            if (type === 3) return elError('数据接口不能作为父节点')
            
            this.type = 'add'
            this.clearForm()
            this.form.pid = id
            
            //如果父节点是叶子菜单，那么只能添加接口
            this.form.type = type === 2 ? 3 : 1
            
            // 确保meta对象正确初始化
            if (!this.form.meta) {
                this.form.meta = {
                    title: '',
                    icon: '',
                    sort: 0
                }
            }
            
            this.form.metaStr = JSON.stringify(this.form.meta, null, 2)
        },
        del() {
            if (this.loading) return
            if (!this.curNode) return elError('请选择要删除的节点')
            elConfirm(`确定删除【${getNodeTitle(this.curNode)}】?`)
                .then(() => {
                    this.loading = true
                    return del.request(this.curNode.data.id)
                })
                .then(() => {
                    elSuccess('删除成功')
                    this.init()
                })
                .finally(() => this.loading = false)
        },
        more({action, level}) {
            if (this.loading) return
            expandControl(this.$refs.tree, action, level)
        },

        init() {
            this.curNode = null
            this.clearForm()
            return this.$store.dispatch('resource/init')
        },
        clearForm() {
            resetObj(this.form)
            this.form.id = null
            this.form.pid = null
            this.form.type = null
            this.form.name = ''
            this.form.path = ''
            this.form.component = ''
            
            // 确保meta对象存在并有默认值
            if (!this.form.meta) {
                this.form.meta = {}
            }
            resetObj(this.form.meta)
            this.form.meta.title = ''
            this.form.meta.icon = ''
            this.form.meta.sort = 0
            
            // 初始化metaStr
            this.form.metaStr = JSON.stringify({
                title: '',
                icon: '',
                sort: 0
            }, null, 2)
            
            this.form.admin = false
            this.form.enable = true
        },
        save() {
            if (this.loading) return
            
            // 对于接口类型的菜单，不需要验证meta.title
            if (this.form.type === 3) {
                this.$refs.form.clearValidate('meta.title')
            }
            
            this.$refs.form.validate(valid => {
                if (!valid) {
                    console.log('表单验证失败')
                    elError('表单验证失败，请检查输入')
                    return false
                }
                
                try {
                    this.loading = true
                    const data = this.transformForm()
                    console.log('提交的数据:', data)
                    
                    if (!data.type && data.type !== 0) {
                        throw new Error('菜单类型不能为空')
                    }
                    
                    const promise = this.type === 'add' ? add.request(data) : update.request(data)
                    promise
                        .then(({data, msg}) => {
                            elSuccess(msg)
                            return this.init()
                        })
                        .catch(error => {
                            console.error('保存失败:', error)
                            elError('保存失败：' + (error.message || '未知错误'))
                        })
                        .finally(() => this.loading = false)
                } catch (e) {
                    this.loading = false
                    elError(e.message || '数据处理错误')
                }
            })
        },
        //将this.form转换为真正的提交数据（meta替换为metaStr）
        transformForm() {
            let meta = {}
            try {
                meta = JSON.parse(this.form.metaStr)
            } catch (e) {
                console.error('Meta JSON解析错误:', e)
                // 如果解析失败，使用现有的meta对象
                meta = {...this.form.meta}
            }
            
            return {
                id: this.form.id,
                pid: this.form.pid,
                name: this.form.name,
                type: this.form.type,
                path: this.form.path,
                component: this.form.component,
                meta: meta,
                admin: this.form.admin,
                enable: this.form.enable
            }
        }
    }
}
</script>

<style scoped>
.button-group {
    margin-bottom: 15px;
}
.tip-row {
    background-color: #ffecec;
    padding: 8px;
    margin-bottom: 15px;
    border-radius: 4px;
    color: #f56c6c;
}
</style>
