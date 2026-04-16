<template>
  <div class="location-manage-container">
    <el-row>
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('locationManage.queryCondition') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-input v-model="locationManageInfo.conditions.locationId"
                :placeholder="$t('locationManage.enterLocationCode')" clearable />
            </el-col>
            <el-col :span="8">
              <el-input v-model="locationManageInfo.conditions.locationName"
                :placeholder="$t('locationManage.enterLocationName')" clearable />
            </el-col>
            <!-- <el-col :span="6">
              <el-select v-model="locationManageInfo.conditions.locationType"
                :placeholder="$t('locationManage.selectLocationType')" style="width:100%" clearable>
                <el-option v-for="item in locationTypeOptions" :key="item.value" :label="item.label"
                  :value="item.value" />
              </el-select>
            </el-col> -->
            <el-col :span="4">
              <el-button type="primary" @click="_queryLocationMethod">
                <i class="el-icon-search"></i>
                {{ $t('common.search') }}
              </el-button>
              <el-button @click="_resetLocationMethod">
                <i class="el-icon-refresh"></i>
                {{ $t('common.reset') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <el-row >
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('locationManage.deviceLocation') }}</span>
            <el-button type="primary" size="small" style="float:right" @click="_openAddLocationModal">
              <i class="el-icon-plus"></i>
              {{ $t('common.add') }}
            </el-button>
          </div>
          <el-table :data="locationManageInfo.locations" border style="width:100%">
            <el-table-column prop="locationId" :label="$t('locationManage.locationCode')" align="center" />
            <el-table-column prop="locationName" :label="$t('locationManage.locationName')" align="center" />
            <el-table-column prop="locationTypeName" :label="$t('locationManage.locationType')" align="center" />
            <el-table-column prop="locationObjName" :label="$t('locationManage.locationObject')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="200">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="_openEditLocationModel(scope.row)">
                  {{ $t('common.edit') }}
                </el-button>
                <el-button size="mini" type="danger" @click="_openDeleteLocationModel(scope.row)">
                  {{ $t('common.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>

    <add-location ref="addLocation" @success="handleSuccess" />
    <edit-location ref="editLocation" @success="handleSuccess" />
    <delete-location ref="deleteLocation" @success="handleSuccess" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listCommunityLocations } from '@/api/community/locationManageApi'
import AddLocation from '@/components/community/AddLocation'
import EditLocation from '@/components/community/EditLocation'
import DeleteLocation from '@/components/community/DeleteLocation'

export default {
  name: 'LocationManageList',
  components: {
    AddLocation,
    EditLocation,
    DeleteLocation
  },
  data() {
    return {
      locationManageInfo: {
        locations: [],
        conditions: {
          locationId: '',
          locationName: '',
          locationType: '',
          communityId: ''
        }
      },
      locationTypeOptions: [
        { value: '1000', label: this.$t('locationManage.community') },
        { value: '6000', label: this.$t('locationManage.building') },
        { value: '2000', label: this.$t('locationManage.unit') },
        { value: '3000', label: this.$t('locationManage.room') },
        { value: '4000', label: this.$t('locationManage.guardBox') },
        { value: '5000', label: this.$t('locationManage.department') }
      ],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this._listLocations(this.page.current, this.page.size)
  },
  methods: {
    async _listLocations(page, size) {
      try {
        this.locationManageInfo.conditions.communityId = getCommunityId()
        const params = {
          page: page,
          row: size,
          ...this.locationManageInfo.conditions
        }
        const { data, total } = await listCommunityLocations(params)
        this.locationManageInfo.locations = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('locationManage.fetchError'))
      }
    },
    _openAddLocationModal() {
      this.$refs.addLocation.open()
    },
    _openEditLocationModel(row) {
      this.$refs.editLocation.open(row)
    },
    _openDeleteLocationModel(row) {
      this.$refs.deleteLocation.open(row)
    },
    _queryLocationMethod() {
      this.page.current = 1
      this._listLocations(this.page.current, this.page.size)
    },
    _resetLocationMethod() {
      this.locationManageInfo.conditions = {
        locationId: '',
        locationName: '',
        locationType: '',
        communityId: getCommunityId()
      }
      this._listLocations(this.page.current, this.page.size)
    },
    handleSuccess() {
      this._listLocations(this.page.current, this.page.size)
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listLocations(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listLocations(this.page.current, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.location-manage-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>