<template>
  <el-card class="border-radius">
    <div class="margin-xs-r treeview attendance-staff" style="height: 650px;">
      <ul class="list-group text-center border-radius">
        <li 
          class="list-group-item node-orgTree" 
          v-for="(item,index) in selectAdminCommunityInfo.communitys" 
          :key="index" 
          @click="_swatchAdminCommunity(item)"
          :class="{'vc-node-selected': selectAdminCommunityInfo.communityId == item.communityId}"
        >
          {{item.name}}
        </li>
      </ul>
    </div>
  </el-card>
</template>

<script>
export default {
  name: 'SelectAdminCommunity',
  data() {
    return {
      selectAdminCommunityInfo: {
        communitys: [],
        communityId: ''
      }
    }
  },
  created() {
    this._loadAdminCommunitys()
  },
  methods: {
    async _loadAdminCommunitys() {
      const param = {
        params: {
          _uid: '123mlkdinkldldijdhuudjdjkkd',
          page: 1,
          row: 100
        }
      }
      
      const _communistys = [{
        name: this.$t('adminMeterRecharge.allCommunities'),
        communityId: ''
      }]
      
      this.selectAdminCommunityInfo.communitys = _communistys
      
      try {
        const res = await this.$api.listAdminCommunitys(param)
        res.data.forEach(c => {
          _communistys.push(c)
        })
        this.selectAdminCommunityInfo.communitys = _communistys
        this._swatchAdminCommunity(_communistys[0])
      } catch (error) {
        console.error('请求失败处理', error)
      }
    },
    _swatchAdminCommunity(_community) {
      this.selectAdminCommunityInfo.communityId = _community.communityId
      this.$emit('changeCommunity', _community)
    }
  }
}
</script>

<style scoped>
.border-radius {
  border-radius: 4px;
}
.margin-xs-r {
  margin-right: 5px;
}
.treeview {
  overflow-y: auto;
}
.list-group {
  padding-left: 0;
  margin-bottom: 0;
}
.list-group-item {
  position: relative;
  display: block;
  padding: 10px 15px;
  margin-bottom: -1px;
  background-color: #fff;
  border: 1px solid #ddd;
  cursor: pointer;
}
.list-group-item:hover {
  background-color: #f5f5f5;
}
.vc-node-selected {
  background-color: #409EFF;
  color: white;
}
</style>