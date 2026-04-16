<template>
  <div class="input-search-owner">
    <el-popover placement="bottom" width="400" trigger="manual" v-model="visible">
      <div class="search-result-container">
        <div class="close-btn" @click="close">
          <i class="el-icon-close"></i>
        </div>
        <div class="search-item" v-for="(item, index) in owners" :key="index" @click="chooseOwner(item)">
          <span>{{ item.name }}</span>
          <span class="separator">---</span>
          <span>{{ item.link }}</span>
        </div>
        
      </div>
      <!-- <el-button slot="reference" type="primary" icon="el-icon-search"></el-button> -->
    </el-popover>
  </div>
</template>

<script>
import { queryOwners } from '@/api/owner/ownerApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'InputSearchOwner',
  data() {
    return {
      visible: false,
      owners: [],
      searchParam: {
        ownerName: '',
        ownerTypeCd: '',
        callComponent: ''
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(param) {
      this.searchParam = { ...param }
      this.loadOwners()
      this.visible = true
    },
    close() {
      this.visible = false
    },
    async loadOwners() {
      try {
        const params = {
          page: 1,
          row: 10,
          ownerTypeCd: this.searchParam.ownerTypeCd,
          name: this.searchParam.ownerName,
          communityId: this.communityId
        }

        const { data } = await queryOwners(params)
        this.owners = data.data || []
        if (this.owners.length === 0) {
          this.$message.warning(this.$t('common.noData'))
        }
      } catch (error) {
        console.error('查询业主失败:', error)
        this.$message.error(this.$t('common.queryFailed'))
      }
    },
    chooseOwner(owner) {
      this.$emit('notifyOwner', owner)
      this.$emit('choose', owner)
      this.close()
    }
  }
}
</script>

<style lang="scss" scoped>
.input-search-owner {
  display: inline-block;

  .search-result-container {
    max-height: 300px;
    overflow-y: auto;
    padding: 10px;

    .close-btn {
      text-align: right;
      margin-bottom: 10px;
      cursor: pointer;

      i {
        font-size: 16px;
        color: #999;

        &:hover {
          color: #333;
        }
      }
    }

    .search-item {
      padding: 8px 10px;
      border-bottom: 1px solid #eee;
      cursor: pointer;
      display: flex;
      align-items: center;

      &:hover {
        background-color: #f5f7fa;
      }

      &:last-child {
        border-bottom: none;
      }

      .separator {
        margin: 0 5px;
        color: #ccc;
      }
    }
  }

  .el-button {
    padding: 10px;
  }
}
</style>