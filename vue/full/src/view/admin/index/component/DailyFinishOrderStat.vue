<template>
    <div :style="{height,width}"/>
</template>

<script>
import {logic, resize} from "@/mixin/chart"
import {getDailyFinishOrder} from '@/api/statistic'
import {timeFormat} from "@/util"

export default {
    name: "DailyFinishOrderStat",

    mixins: [resize, logic],

    methods: {
        init() {
            if (this.loading) return
            this.loading = true
            getDailyFinishOrder
                .request()
                .then(({data}) => {
                    const time = []
                    const sell = []
                    data.forEach(i => {
                        time.push(timeFormat('MM 月 dd日', new Date(i.time)))
                        sell.push(i.sell)
                    })
                    this.setOptions({time, sell})
                })
                .finally(() => this.loading = false)
        },

        setOptions({time, sell}) {
            this.chart.setOption({
                title: {
                    text: '历史七天订单完成情况统计',
                    left: 'center',
                    align: 'right'
                },
                xAxis: {
                    data: time,
                    axisTick: {
                        show: false
                    }
                },
                grid: {
                    left: 10,
                    right: 10,
                    bottom: 20,
                    top: 30,
                    containLabel: true
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross'
                    },
                    padding: [5, 10]
                },
                yAxis: {
                    axisTick: {
                        show: false
                    }
                },
                legend: {
                    data: ['销售订单'],
                    right: 10
                },
                series: [
                    {
                        name: '销售订单',
                        smooth: true,
                        type: 'line',
                        data: sell
                    }
                ]
            })
        }
    }
}
</script>
