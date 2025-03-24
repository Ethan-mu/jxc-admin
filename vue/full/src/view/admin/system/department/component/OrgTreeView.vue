<template>
    <div class="org-tree-container">
        <div
            v-if="data"
            :class="['org-tree', {'horizontal': horizontal}]"
            :style="{transform: `scale(${zoom})`, transformOrigin: '50% 0'}"
        >
            <org-tree-node
                :data="data"
                :props="props"
                :horizontal="horizontal"
                :label-width="labelWidth"
                :collapsable="collapsable"
                :render-content="renderContent"
                :label-class-name="labelClassName"
                @on-expand="handleExpand"
                @on-node-click="(e, data) => $emit('node-click', e, data)"
                @on-node-mouseover="(e, data) => $emit('node-mouseover', e, data)"
                @on-node-mouseout="(e, data) => $emit('node-mouseout', e, data)"
                @on-node-contextmenu="(e, data) => $emit('node-contextmenu', e, data)"
            />
        </div>
        <div v-else class="org-tree-empty">暂无数据</div>
    </div>
</template>

<script>
/**
 * 该组件使用vue-org-tree
 * GitHub: https://github.com/huangwei9527/vue-org-tree
 * 安装：npm install vue-org-tree --save
 */
import OrgTreeNode from './OrgTreeNode'

export default {
    name: "OrgTreeView",
    
    components: {OrgTreeNode},
    
    props: {
        data: {
            type: Object,
            required: true
        },
        props: {
            type: Object,
            default: () => ({
                label: 'label',
                expand: 'expand',
                children: 'children'
            })
        },
        horizontal: Boolean,
        collapsable: Boolean,
        renderContent: Function,
        labelWidth: {
            type: [String, Number],
            default: 'auto'
        },
        labelClassName: [Function, String],
        zoom: {
            type: Number,
            default: 1
        }
    },
    
    methods: {
        handleExpand(e, data) {
            this.$emit('on-expand', e, data)
        }
    }
}
</script>

<style lang="scss">
.org-tree-container {
    display: inline-block;
    padding: 15px;
    width: 100%;
    height: 100%;
    box-sizing: border-box;
    overflow: auto;
    
    .org-tree {
        display: inline-block;
        min-height: 100%;
        min-width: 100%;
        
        &.horizontal {
            display: flex;
            align-items: center;
            min-height: 100%;
        }
    }
    
    .org-tree-empty {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100%;
        color: #909399;
        font-size: 14px;
    }
}

.org-tree-node-label {
    cursor: pointer;
    
    &:hover {
        background-color: rgba(100, 142, 255, 0.1);
        color: #2d8cf0;
    }
}
</style>
