<template>
  <div class="org-tree-node" @click.stop>
    <div class="org-tree-node-label-wrapper">
      <div
        :class="[
          'org-tree-node-label',
          labelClasses,
          { collapsed: data[props.expand] === false }
        ]"
        :style="{ width: labelWidth }"
        @click="handleClick"
        @mouseover="handleMouseover"
        @mouseout="handleMouseout"
        @contextmenu="handleContextmenu"
      >
        <div class="org-tree-node-label-inner" :title="data[props.label]">
          <slot v-if="renderContent" :props="{ data, props }"></slot>
          <template v-else>
            {{ data[props.label] || '-' }}
          </template>
        </div>
        <span
          v-if="collapsable && childNodes.length"
          :class="['org-tree-node-btn', { expanded: expanded }]"
          @click.stop="handleExpand"
        ></span>
      </div>
    </div>
    <div
      v-if="childNodes.length && expanded"
      :class="[
        'org-tree-node-children',
        { horizontal, expanded }
      ]"
    >
      <org-tree-node
        v-for="(item, index) in childNodes"
        :key="index"
        :data="item"
        :props="props"
        :horizontal="horizontal"
        :collapsable="collapsable"
        :render-content="renderContent"
        :label-class-name="labelClassName"
        :label-width="labelWidth"
        @on-expand="$emit('on-expand', $event, $event.data)"
        @on-node-click="$emit('on-node-click', $event, $event.data)"
        @on-node-mouseover="$emit('on-node-mouseover', $event, $event.data)"
        @on-node-mouseout="$emit('on-node-mouseout', $event, $event.data)"
        @on-node-contextmenu="$emit('on-node-contextmenu', $event, $event.data)"
      />
    </div>
  </div>
</template>

<script>
export default {
  name: 'OrgTreeNode',
  
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
    labelClassName: [Function, String]
  },
  
  computed: {
    childNodes() {
      return this.data[this.props.children] || []
    },
    expanded() {
      return this.data[this.props.expand] !== false
    },
    labelClasses() {
      const classes = []
      if (typeof this.labelClassName === 'function') {
        classes.push(this.labelClassName(this.data))
      } else if (this.labelClassName) {
        classes.push(this.labelClassName)
      }
      return classes
    }
  },
  
  methods: {
    handleExpand(e) {
      e.stopPropagation()
      this.$set(this.data, this.props.expand, !this.expanded)
      this.$emit('on-expand', e)
    },
    
    handleClick(e) {
      this.$emit('on-node-click', e, this.data)
    },
    
    handleMouseover(e) {
      this.$emit('on-node-mouseover', e, this.data)
    },
    
    handleMouseout(e) {
      this.$emit('on-node-mouseout', e, this.data)
    },
    
    handleContextmenu(e) {
      this.$emit('on-node-contextmenu', e, this.data)
    }
  }
}
</script>

<style lang="scss">
.org-tree-node {
  position: relative;
  display: inline-block;
  vertical-align: middle;
  padding: 0 0 10px 0;
  
  &:before,
  &:after {
    position: absolute;
    content: '';
    background-color: #ddd;
  }
  
  &:before {
    left: 50%;
    top: 0;
    width: 1px;
    height: 10px;
    margin-left: -0.5px;
  }
  
  &:after {
    left: 0;
    right: 0;
    top: 10px;
    height: 1px;
  }
  
  &:first-child:after {
    left: 50%;
  }
  
  &:last-child:after {
    right: 50%;
  }
  
  &:only-child:after {
    left: 0;
    width: 0;
  }
  
  &:only-child:before {
    height: 15px;
  }
  
  .org-tree-node-label-wrapper {
    position: relative;
    display: inline-block;
    z-index: 2;
    
    .org-tree-node-label {
      position: relative;
      display: inline-block;
      padding: 5px 10px;
      text-align: center;
      background-color: #fff;
      border: 1px solid #ddd;
      border-radius: 3px;
      
      &.collapsed {
        background-color: #f5f5f5;
      }
      
      .org-tree-node-label-inner {
        padding: 0 4px;
        text-overflow: ellipsis;
        overflow: hidden;
      }
      
      .org-tree-node-btn {
        position: absolute;
        bottom: -11px;
        left: 50%;
        width: 16px;
        height: 16px;
        margin-left: -8px;
        background-color: #fff;
        border: 1px solid #ddd;
        border-radius: 50%;
        z-index: 10;
        cursor: pointer;
        
        &:hover {
          background-color: #f9f9f9;
          border-color: #999;
        }
        
        &:before {
          content: '';
          display: block;
          position: absolute;
          top: 4px;
          left: 4px;
          width: 6px;
          height: 6px;
          border-top: 1px solid #000;
          border-right: 1px solid #000;
          transform: rotate(135deg);
          transition: transform 0.2s;
        }
        
        &.expanded:before {
          transform: rotate(-45deg);
        }
      }
    }
  }
  
  .org-tree-node-children {
    position: relative;
    padding-top: 10px;
    white-space: nowrap;
    
    &:before {
      position: absolute;
      content: '';
      left: 50%;
      top: 0;
      width: 1px;
      height: 10px;
      margin-left: -0.5px;
      background-color: #ddd;
    }
    
    &.horizontal {
      display: flex;
      
      .org-tree-node {
        display: flex;
        flex-direction: column;
        padding: 10px 0 0 0;
        
        &:before,
        &:after {
          position: absolute;
          content: '';
          background-color: #ddd;
        }
        
        &:before {
          top: 0;
          left: 10px;
          right: 10px;
          width: auto;
          height: 1px;
          margin-left: 0;
        }
        
        &:after {
          top: 1px;
          left: 50%;
          bottom: 0;
          width: 1px;
          height: auto;
          margin-left: -0.5px;
        }
        
        &:first-child:before {
          left: 50%;
        }
        
        &:last-child:before {
          right: 50%;
        }
        
        &:only-child:before {
          left: 10px;
          right: 10px;
        }
        
        &:only-child:after {
          top: 1px;
          height: 10px;
        }
        
        .org-tree-node-label-wrapper {
          display: block;
        }
      }
    }
  }
}
</style> 