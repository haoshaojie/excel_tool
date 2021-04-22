<!--客户管理活动-->
<template>
  <basic-container>
    <avue-crud
      :option="option"
      :data="caseInfoList.orgActivities"
    ></avue-crud>
  </basic-container>
</template>
<script>
  export default {
    name: "contract-info",
    props: {
      caseInfoList: {
        type: Object,
        required: true
      },
    },
    data() {
      return {
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 1
        },
        option: {
          addBtn: false,
          editBtn: false,
          delBtn: false,
          refreshBtn: false,
          columnBtn: false,
          header: false,
          border: true,
          viewBtn: true,
          menu:false,
          viewBtnText: "查看详情",
          column: [
            {
              label: '活动编码',
              prop: 'activityNo'
            },
            {
              label: '活动类型',
              prop: 'activityTypeName'
            },
            {
              label: '具体内容',
              prop: 'activityContent'
            }
          ]
        },
        data: [
          {
            activityCode: "活动编码",
            activityType: "活动类型",
            activityContent: "具体内容"
          }
        ]
      }
    },
    methods: {}
  }
</script>
