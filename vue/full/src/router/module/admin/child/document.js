/*路由表：档案管理*/

const router = {
    path: 'document',
    meta: {title: '档案管理', icon: 'svg-document'},
    children: [
        {
            path: 'qualification',
            name: 'companyQualification',
            component: 'admin/document/qualification/',
            meta: {title: '公司资质证书管理'}
        },
        {
            path: 'report',
            name: 'reportManagement',
            component: 'admin/document/report/',
            meta: {title: '报告管理'}
        },
        {
            path: 'insurance',
            name: 'enterpriseInsuranceManagement',
            component: 'admin/document/insurance/',
            meta: {title: '企业保险管理'}
        },
        {
            path: 'safety-staff',
            name: 'safetyStaffCert',
            component: 'admin/document/safety-staff/',
            meta: {title: '安全管理人员证书管理'}
        },
        {
            path: 'specialOperationWorkerCert',
            component: () => import('@/view/admin/document/specialOperationWorkerCert/indexPage'),
            name: 'specialOperationWorkerCert',
            meta: { title: '特种作业人员证书', breadcrumb: true }
        },
        {
            path: 'specialEquipmentWorkerCert',
            component: () => import('@/view/admin/document/specialEquipmentWorkerCert/indexPage'),
            name: 'specialEquipmentWorkerCert',
            meta: { title: '特种设备操作作业人员证书', breadcrumb: true }
        },
        {
            path: 'special-equipment',
            name: 'specialEquipment',
            component: 'admin/document/special-equipment/',
            meta: {title: '特种设备管理'}
        },
        {
            path: 'emergency-supplies',
            name: 'emergencySupplies',
            component: () => import('@/view/admin/document/emergency-supplies/indexPage'),
            meta: {title: '应急物资管理', breadcrumb: true}
        },
        {
            path: 'safety-attachment',
            name: 'safetyAttachment',
            component: () => import('@/view/admin/document/safety-attachment/indexPage'),
            meta: {title: '安全附件管理', breadcrumb: true}
        },
        {
            path: 'electric-tool',
            name: 'electricTool',
            component: () => import('@/view/admin/document/electric-tool/indexPage'),
            meta: {title: '电动防护工器具管理', breadcrumb: true}
        },
        {
            path: 'safety-inspection',
            name: 'safetyInspection',
            component: () => import('@/view/admin/document/safety-inspection/indexPage'),
            meta: {title: '安全隐患排查管理', breadcrumb: true}
        }
    ]
}

export default router 