/*路由表：安全教育培训*/

const router = {
    path: 'safety-training',
    meta: {title: '安全教育培训', icon: 'svg-education'},
    children: [
        {
            path: 'three-level-training',
            name: 'threeLevelTraining',
            component: () => import('@/view/admin/document/three-level-training/indexPage'),
            meta: {title: '三级教育培训', breadcrumb: true}
        },
        {
            path: 'four-new-training',
            name: 'fourNewTraining',
            component: () => import('@/view/admin/training/four-new-training/indexPage'),
            meta: {title: '四新技术培训', breadcrumb: true}
        },
        {
            path: 'return-job-training',
            name: 'returnJobTraining',
            component: () => import('@/view/admin/training/return-job-training/indexPage'),
            meta: {title: '转岗教育培训', breadcrumb: true}
        },
        {
            path: 'return-job-training-back',
            name: 'returnJobTrainingBack',
            component: () => import('@/view/admin/training/return-post-training/indexPage'),
            meta: {title: '复岗教育培训', breadcrumb: true}
        },
        {
            path: 'continuing-education',
            name: 'continuingEducation',
            component: () => import('@/view/admin/training/continuing-education/indexPage'),
            meta: {title: '继续教育培训', breadcrumb: true}
        },
        {
            path: 'unplanned-training',
            name: 'unplannedTraining',
            component: () => import('@/view/admin/training/unplanned-training/indexPage'),
            meta: {title: '计划外培训', breadcrumb: true}
        }
    ]
}

export default router 