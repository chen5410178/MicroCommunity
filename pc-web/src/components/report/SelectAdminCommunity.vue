<template>
  <div class="select-admin-community">
    <el-card class="border-radius">
      <div class="treeview attendance-staff" style="height: 650px;">
        <ul class="list-group text-center border-radius">
          <li
            v-for="(item, index) in communityList"
            :key="index"
            class="list-group-item node-orgTree"
            :class="{ 'vc-node-selected': selectedCommunityId === item.communityId }"
            @click="handleCommunityClick(item)"
          >
            {{ item.name }}
          </li>
        </ul>
      </div>
    </el-card>
  </div>
</template>

<script>
import { listAdminCommunitys } from '@/api/report/communityFeeSummaryApi'

export default {
  name: 'SelectAdminCommunity',
  data() {
    return {
      communityList: [
        { name: this.$t('communityFeeSummary.allCommunities'), communityId: '' }
      ],
      selectedCommunityId: ''
    }
  },
  created() {
    this.loadAdminCommunities()
  },
  methods: {
    async loadAdminCommunities() {
      try {
        const params = {
          _uid: '123mlkdinkldldijdhuudjdjkkd',
          page: 1,
          row: 100
        }
        
        const { data } = await listAdminCommunitys(params)
        this.communityList = [...this.communityList, ...data]
      } catch (error) {
        console.error('Failed to load communities:', error)
      }
    },
    handleCommunityClick(community) {
      this.selectedCommunityId = community.communityId
      this.$emit('changeCommunity', community)
    }
  }
}
</script>

<style lang="scss" scoped>
.select-admin-community {
  .border-radius {
    border-radius: 4px;
  }
  
  .treeview {
    overflow-y: auto;
  }
  
  .list-group {
    padding-left: 0;
    margin-bottom: 0;
    
    &-item {
      position: relative;
      display: block;
      padding: 10px 15px;
      margin-bottom: -1px;
      background-color: #fff;
      border: 1px solid #ddd;
      cursor: pointer;
      
      &:first-child {
        border-top-left-radius: 4px;
        border-top-right-radius: 4px;
      }
      
      &:last-child {
        margin-bottom: 0;
        border-bottom-right-radius: 4px;
        border-bottom-left-radius: 4px;
      }
      
      &.vc-node-selected {
        background-color: #409EFF;
        color: #fff;
        border-color: #409EFF;
      }
      
      &:hover:not(.vc-node-selected) {
        background-color: #f5f5f5;
      }
    }
  }
}
</style>