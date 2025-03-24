<template>
    <div class="zoom-control">
        <el-button
            circle
            icon="el-icon-minus"
            size="small"
            title="缩小"
            type="info"
            @click="decrease"
        />
        <span class="zoom-text">{{ zoom }}%</span>
        <el-button
            circle
            icon="el-icon-plus"
            size="small"
            title="放大"
            type="info"
            @click="increase"
        />
    </div>
</template>

<script>
export default {
    name: "ZoomControl",
    
    props: {
        value: {
            type: Number,
            default: 100
        },
        step: {
            type: Number,
            default: 10
        },
        min: {
            type: Number,
            default: 50
        },
        max: {
            type: Number,
            default: 150
        }
    },
    
    computed: {
        zoom: {
            get() {
                return this.value
            },
            set(val) {
                let value = Math.min(Math.max(val, this.min), this.max)
                this.$emit('input', value)
            }
        }
    },
    
    methods: {
        increase() {
            this.zoom += this.step
        },
        
        decrease() {
            this.zoom -= this.step
        }
    }
}
</script>

<style lang="scss" scoped>
.zoom-control {
    display: flex;
    align-items: center;
    
    .zoom-text {
        display: inline-block;
        width: 50px;
        text-align: center;
        font-size: 14px;
        color: #606266;
    }
}
</style>
