<template>
  <div class="org-tree-container">
    <el-tree
      ref="orgTree"
      :data="orgTreeShowInfo.orgs"
      :props="defaultProps"
      node-key="id"
      default-expand-all
      highlight-current
      @node-click="handleNodeClick"
    ></el-tree>
  </div>
</template>

<script>
import { listOrgTree } from '@/api/contract/contractChangeDetailApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'OrgTreeShow',
  props: {
    callBackListener: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      orgTreeShowInfo: {
        orgs: [],
        orgId: '',
        curOrg: {}
      },
      defaultProps: {
        children: 'children',
        label: 'text'
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    loadOrgsShow() {
      const param = {
        communityId: this.communityId
      }

      listOrgTree(param)
        .then(response => {
          const orgs = response.data
          this.orgTreeShowInfo.orgs = orgs
        })
        .catch(error => {
          console.error('请求失败:', error)
        })
    },
    handleNodeClick(data) {
      this.orgTreeShowInfo.curOrg = data
      this.orgTreeShowInfo.curOrg.orgId = data.id
      this.$emit(this.callBackListener, 'switchOrg', {
        orgId: data.id,
        orgName: data.text
      })
    },
    refreshTree() {
      this.loadOrgsShow()
    }
  }
}
</script>

<style scoped>
.org-tree-container {
  height: 100%;
  overflow-y: auto;
}

.el-tree {
  min-height: 400px;
}
</style>