<template>
  <div class="community-space-manage padding">
    <el-row :gutter="10">
      <el-col :span="4">
        <el-card class="venue-list">
          <div class="button-group">
            <el-button type="primary" size="small" @click="openAddVenueModal">
              {{ $t('communitySpaceManage.add') }}
            </el-button>
            <el-button type="primary" size="small" @click="openEditVenueModal">
              {{ $t('communitySpaceManage.edit') }}
            </el-button>
            <el-button type="danger" size="small" @click="openDeleteVenueModal">
              {{ $t('communitySpaceManage.delete') }}
            </el-button>
          </div>
          <el-scrollbar class="venue-scrollbar">
            <ul class="venue-items">
              <li v-for="(item, index) in venues" :key="index" :class="{ 'active': conditions.venueId === item.venueId }"
                @click="selectVenue(item)">
                {{ item.name }}
              </li>
            </ul>
          </el-scrollbar>
        </el-card>
      </el-col>
      <el-col :span="20">
        <el-card>
          <div slot="header" class="clearfix text-left">
            <span>{{ $t('communitySpaceManage.query') }}</span>
          </div>
          <el-form :inline="true" :model="conditions" class="search-form">
            <el-form-item >
              <el-input v-model="conditions.spaceId" :placeholder="$t('communitySpaceManage.selectSpace')"></el-input>
            </el-form-item>
            <el-form-item >
              <el-input v-model="conditions.name" :placeholder="$t('communitySpaceManage.selectSpace')"></el-input>
            </el-form-item>
            <el-form-item >
              <el-select v-model="conditions.state" :placeholder="$t('communitySpaceManage.selectStatus')"
                style="width:100%">
                <el-option :label="$t('communitySpaceManage.selectStatus')" value=""></el-option>
                <el-option :label="$t('communitySpaceManage.status1001')" value="1001"></el-option>
                <el-option :label="$t('communitySpaceManage.status2002')" value="2002"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="querySpaces">
                <i class="el-icon-search"></i>
                {{ $t('communitySpaceManage.query') }}
              </el-button>
              <el-button @click="resetConditions">
                <i class="el-icon-refresh"></i>
                {{ $t('communitySpaceManage.reset') }}
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card style="margin-top: 20px;">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('communitySpaceManage.spaceInfo') }}</span>
            <div style="float: right;">
              <el-button type="primary" size="small" @click="openAddSpaceModal">
                <i class="el-icon-plus"></i>
                {{ $t('communitySpaceManage.addSpace') }}
              </el-button>
            </div>
          </div>
          <el-table :data="spaces" border style="width: 100%">
            <el-table-column prop="spaceId" :label="$t('communitySpaceManage.spaceId')" align="center" />
            <el-table-column prop="name" :label="$t('communitySpaceManage.name')" align="center" />
            <el-table-column prop="startTime" :label="$t('communitySpaceManage.startTime')" align="center" />
            <el-table-column prop="endTime" :label="$t('communitySpaceManage.endTime')" align="center" />
            <el-table-column prop="feeMoney" :label="$t('communitySpaceManage.feeMoney')" align="center" />
            <el-table-column prop="adminName" :label="$t('communitySpaceManage.adminName')" align="center" />
            <el-table-column prop="tel" :label="$t('communitySpaceManage.tel')" align="center" />
            <el-table-column prop="state" :label="$t('communitySpaceManage.state')" align="center">
              <template slot-scope="scope">
                {{ scope.row.state === '1001' ? $t('communitySpaceManage.status1001') :
                  $t('communitySpaceManage.status2002') }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('communitySpaceManage.operation')" align="center" width="300">
              <template slot-scope="scope">
                <el-button size="mini" @click="openEditOpenTime(scope.row)">
                  {{ $t('communitySpaceManage.openTime') }}
                </el-button>
                <el-button size="mini" type="primary" @click="openEditSpaceModal(scope.row)">
                  {{ $t('communitySpaceManage.edit') }}
                </el-button>
                <el-button size="mini" type="danger" @click="openDeleteSpaceModal(scope.row)">
                  {{ $t('communitySpaceManage.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination style="margin-top: 20px;" @size-change="handleSizeChange" @current-change="handlePageChange"
            :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            layout="total, sizes, prev, pager, next, jumper" :total="total">
          </el-pagination>
        </el-card>
      </el-col>
    </el-row>

    <!-- 子组件 -->
    <add-community-space ref="addSpaceModal" @success="listSpaces" />
    <edit-community-space ref="editSpaceModal" @success="listSpaces" />
    <edit-community-space-open-time ref="editOpenTimeModal" />
    <delete-community-space ref="deleteSpaceModal" @success="listSpaces" />
    <add-community-venue ref="addVenueModal" @success="listVenues" />
    <edit-community-venue ref="editVenueModal" @success="listVenues" />
    <delete-community-venue ref="deleteVenueModal" @success="listVenues" />
  </div>
</template>

<script>
import { listCommunityVenue, listCommunitySpace } from '@/api/community/communitySpaceManageApi'
import AddCommunitySpace from '@/components/community/AddCommunitySpace'
import EditCommunitySpace from '@/components/community/EditCommunitySpace'
import EditCommunitySpaceOpenTime from '@/components/community/EditCommunitySpaceOpenTime'
import DeleteCommunitySpace from '@/components/community/DeleteCommunitySpace'
import AddCommunityVenue from '@/components/community/AddCommunityVenue'
import EditCommunityVenue from '@/components/community/EditCommunityVenue'
import DeleteCommunityVenue from '@/components/community/DeleteCommunityVenue'

export default {
  name: 'CommunitySpaceManage',
  components: {
    AddCommunitySpace,
    EditCommunitySpace,
    EditCommunitySpaceOpenTime,
    DeleteCommunitySpace,
    AddCommunityVenue,
    EditCommunityVenue,
    DeleteCommunityVenue
  },
  data() {
    return {
      venues: [],
      spaces: [],
      conditions: {
        spaceId: '',
        name: '',
        state: '',
        venueId: '',
        page: 1,
        row: 10
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  computed: {
    total() {
      return this.page.total
    }
  },
  created() {
    this.listVenues()
  },
  methods: {
    async listVenues() {
      try {
        const res = await listCommunityVenue({
          page: 1,
          row: 100
        })
        this.venues = res.data
        if (this.venues.length > 0) {
          this.selectVenue(this.venues[0])
        }
      } catch (error) {
        console.error('获取场馆列表失败:', error)
      }
    },

    async listSpaces() {
      try {
        const res = await listCommunitySpace(this.conditions)
        this.spaces = res.data
        this.page.total = res.total
      } catch (error) {
        console.error('获取场地列表失败:', error)
      }
    },

    selectVenue(venue) {
      this.conditions.venueId = venue.venueId
      this.listSpaces()
    },

    querySpaces() {
      this.conditions.page = 1
      this.listSpaces()
    },

    resetConditions() {
      this.conditions.spaceId = ''
      this.conditions.name = ''
      this.conditions.state = ''
      this.querySpaces()
    },

    handlePageChange(page) {
      this.conditions.page = page
      this.listSpaces()
    },

    handleSizeChange(size) {
      this.conditions.row = size
      this.listSpaces()
    },

    openAddSpaceModal() {
      if (!this.conditions.venueId) {
        this.$message.warning(this.$t('communitySpaceManage.selectVenue'))
        return
      }
      this.$refs.addSpaceModal.open(this.conditions.venueId)
    },

    openEditSpaceModal(space) {
      this.$refs.editSpaceModal.open(space)
    },

    openEditOpenTime(space) {
      this.$refs.editOpenTimeModal.open(space)
    },

    openDeleteSpaceModal(space) {
      this.$refs.deleteSpaceModal.open(space)
    },

    openAddVenueModal() {
      this.$refs.addVenueModal.open()
    },

    openEditVenueModal() {
      if (!this.conditions.venueId) {
        this.$message.warning(this.$t('communitySpaceManage.selectVenue'))
        return
      }
      const venue = this.venues.find(v => v.venueId === this.conditions.venueId)
      if (venue) {
        this.$refs.editVenueModal.open(venue)
      }
    },

    openDeleteVenueModal() {
      if (!this.conditions.venueId) {
        this.$message.warning(this.$t('communitySpaceManage.selectVenue'))
        return
      }
      const venue = this.venues.find(v => v.venueId === this.conditions.venueId)
      if (venue) {
        this.$refs.deleteVenueModal.open(venue)
      }
    }
  }
}
</script>

<style scoped>
.venue-list {
  height: 100%;
}

.button-group {
  margin-bottom: 15px;
}

.venue-scrollbar {
  height: calc(100vh - 200px);
}

.venue-items {
  list-style: none;
  padding: 0;
  margin: 0;
}

.venue-items li {
  padding: 12px 15px;
  cursor: pointer;
  transition: all 0.3s;
}

.venue-items li:hover {
  background-color: #f5f7fa;
}

.venue-items li.active {
  background-color:#409eff;
  color:  #ecf5ff;
  font-weight: bold;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
}
</style>