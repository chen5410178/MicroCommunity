<template>
  <div class="community-publicity-manage-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <el-card class="tree-card">
          <ul class="pub-type-list">
            <li v-for="(item, index) in pubTypes" :key="index" class="pub-type-item"
              :class="{ 'active': conditions.pubType === item.statusCd }" @click="handlePubTypeChange(item)">
              {{ item.name }}
            </li>
          </ul>
        </el-card>
      </el-col>
      <el-col :span="20">
        <el-card class="search-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('communityPublicityManage.searchTitle') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-input v-model="conditions.title" :placeholder="$t('communityPublicityManage.titlePlaceholder')"
                clearable />
            </el-col>
            <el-col :span="8">
              <el-button type="primary" @click="handleSearch">
                {{ $t('common.search') }}
              </el-button>
              <el-button @click="handleReset">
                {{ $t('common.reset') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="table-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('communityPublicityManage.tableTitle') }}</span>
            <el-button type="primary" size="small" @click="handleAdd">
              {{ $t('common.add') }}
            </el-button>
          </div>
          <el-table :data="tableData" border style="width: 100%" v-loading="loading">
            <el-table-column prop="headerImg" :label="$t('communityPublicityManage.headerImg')" width="120"
              align="center">
              <template slot-scope="scope">
                <el-image style="width: 60px; height: 60px; cursor: pointer;"
                  :src="scope.row.headerImg || '/img/noPhoto.jpg'"
                  :preview-src-list="[scope.row.headerImg || '/img/noPhoto.jpg']" fit="cover" class="border-radius" />
              </template>
            </el-table-column>
            <el-table-column prop="title" :label="$t('communityPublicityManage.title')" align="center" />
            <el-table-column prop="pubTypeName" :label="$t('communityPublicityManage.pubType')" align="center" />
            <el-table-column prop="createTime" :label="$t('communityPublicityManage.createTime')" align="center" />
            <el-table-column prop="createUserName" :label="$t('communityPublicityManage.publisher')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="200">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
                  {{ $t('common.edit') }}
                </el-button>
                <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
                  {{ $t('common.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" style="margin-top: 20px;" />
        </el-card>
      </el-col>
    </el-row>

    <view-image ref="viewImage" />
    <delete-community-publicity ref="deleteDialog" @success="fetchData" />
  </div>
</template>

<script>
import { getCommunityPublicityList } from '@/api/community/communityPublicityManageApi'
import { getDict } from '@/api/community/communityApi'
import ViewImage from '@/components/system/viewImage'
import DeleteCommunityPublicity from '@/components/community/deleteCommunityPublicity'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CommunityPublicityManageList',
  components: {
    ViewImage,
    DeleteCommunityPublicity
  },
  data() {
    return {
      loading: false,
      pubTypes: [],
      tableData: [],
      conditions: {
        pubType: '',
        title: '',
        communityId: ''
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.conditions.communityId = getCommunityId()
    this.initData()
  },
  methods: {
    async initData() {
      try {
        this.loading = true
        await this.getPubTypes()
        if (this.pubTypes.length > 0) {
          this.conditions.pubType = this.pubTypes[0].statusCd
        }
        await this.fetchData()
      } catch (error) {
        console.error('初始化数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    async getPubTypes() {
      try {
        const data = await getDict('community_publicity', 'pub_type')
        this.pubTypes = data
      } catch (error) {
        console.error('获取公示类型失败:', error)
      }
    },
    async fetchData() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.conditions
        }
        const { data, total } = await getCommunityPublicityList(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        console.error('获取公示列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    handlePubTypeChange(item) {
      this.conditions.pubType = item.statusCd
      this.pagination.current = 1
      this.fetchData()
    },
    handleSearch() {
      this.pagination.current = 1
      this.fetchData()
    },
    handleReset() {
      this.conditions.title = ''
      this.pagination.current = 1
      this.fetchData()
    },
    handleAdd() {
      this.$router.push('/views/community/addCommunityPublicity')
    },
    handleEdit(row) {
      this.$router.push(`/views/community/editCommunityPublicity?pubId=${row.pubId}`)
    },
    handleDelete(row) {
      this.$refs.deleteDialog.open(row)
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.fetchData()
    }
  }
}
</script>

<style lang="scss" scoped>
.community-publicity-manage-container {
  padding: 20px;

  .tree-card {
    height: 100%;

    .pub-type-list {
      list-style: none;
      padding: 0;
      margin: 0;

      .pub-type-item {
        padding: 10px 15px;
        cursor: pointer;
        border-radius: 4px;
        margin-bottom: 5px;
        transition: all 0.3s;

        &:hover {
          background-color: #f5f7fa;
        }

        &.active {
          background-color: #409eff;
          color: white;
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
  }

  .border-radius {
    border-radius: 4px;
  }
}
</style>