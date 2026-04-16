<template>
  <el-card class="community-selector">
    <div class="treeview attendance-staff">
      <ul class="community-list">
        <li 
          v-for="(item, index) in communityList" 
          :key="index" 
          @click="selectCommunity(item)"
          :class="{'selected': selectedCommunityId === item.communityId}">
          {{ item.name }}
        </li>
      </ul>
    </div>
  </el-card>
</template>

<script>
import { listAdminCommunitys } from '@/api/owner/adminOwnerApi'

export default {
  name: 'SelectAdminCommunity',
  data() {
    return {
      communityList: [],
      selectedCommunityId: ''
    }
  },
  created() {
    this.loadCommunities()
  },
  methods: {
    loadCommunities() {
      const params = {
        page: 1,
        row: 100
      }
      
      listAdminCommunitys(params).then(res => {
        this.communityList = [{ name: this.$t('adminOwner.all'), communityId: '' }, ...res.data]
      }).catch(error => {
        console.log(error)
        this.$message.error(this.$t('adminOwner.fetchCommunityError'))
      })
    },
    selectCommunity(community) {
      this.selectedCommunityId = community.communityId
      this.$emit('changeCommunity', community)
    }
  }
}
</script>

<style lang="scss" scoped>
.community-selector {
  max-height: 80vh;
  overflow-y: auto;
  .community-list {
    list-style: none;
    padding: 0;
    margin: 0;
    
    li {
      padding: 10px;
      cursor: pointer;
      transition: all 0.3s;
      color: #606060;
      font-size: 14px;
      
      &:hover {
        background-color: #f5f7fa;
      }
      
      &.selected {
        background-color: #409EFF;
        color: white;
      }
    }
  }
}
</style>