<template>
  <div class="property-right-registration-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('propertyRightRegistration.search.title') }}</span>
        <el-button type="text" style="float: right; padding: 3px 0" @click="toggleMoreCondition">
          {{ showMoreCondition ? $t('common.hide') : $t('common.more') }}
        </el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.roomId" :placeholder="$t('propertyRightRegistration.search.roomId')"
            clearable />
        </el-col>
        <el-col :span="8">
          <el-input v-model="searchForm.allNum" :placeholder="$t('propertyRightRegistration.search.allNum')"
            clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.name" :placeholder="$t('propertyRightRegistration.search.name')" clearable />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="handleReset">
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>

      <!-- 更多查询条件 -->
      <div v-show="showMoreCondition">
        <el-row :gutter="20" class="mt-20">
          <el-col :span="6">
            <el-input v-model="searchForm.link" :placeholder="$t('propertyRightRegistration.search.link')" clearable />
          </el-col>
          <el-col :span="8">
            <el-input v-model="searchForm.idCard" :placeholder="$t('propertyRightRegistration.search.idCard')"
              clearable />
          </el-col>
          <el-col :span="6">
            <el-input v-model="searchForm.address" :placeholder="$t('propertyRightRegistration.search.address')"
              clearable />
          </el-col>
        </el-row>

        <el-row :gutter="20" class="mt-20">
          <el-col :span="6">
            <el-select v-model="searchForm.state" :placeholder="$t('propertyRightRegistration.search.state')"
              style="width:100%">
              <el-option v-for="item in states" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
            </el-select>
          </el-col>
          <el-col :span="8">
            <el-select v-model="searchForm.floorId" :placeholder="$t('propertyRightRegistration.search.floor')"
              style="width:100%" @change="handleFloorChange">
              <el-option v-for="item in floors" :key="item.floorId" :label="item.floorName" :value="item.floorId" />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select v-model="searchForm.unitId" :placeholder="$t('propertyRightRegistration.search.unit')"
              style="width:100%">
              <el-option v-for="item in units" :key="item.unitId"
                :label="`${item.unitNum}${$t('propertyRightRegistration.unit')}`" :value="item.unitId" />
            </el-select>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <!-- 数据列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('propertyRightRegistration.list.title') }}</span>
        <el-button type="primary" size="small" style="float: right" @click="handleAdd">
          <i class="el-icon-plus" />
          {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="prrId" :label="$t('propertyRightRegistration.table.prrId')" align="center" />
        <el-table-column prop="roomId" :label="$t('propertyRightRegistration.table.roomId')" align="center" />
        <el-table-column :label="$t('propertyRightRegistration.table.roomNum')" align="center">
          <template slot-scope="scope">
            {{ `${scope.row.floorNum}-${scope.row.unitNum}-${scope.row.roomNum}` }}
          </template>
        </el-table-column>
        <el-table-column prop="name" :label="$t('propertyRightRegistration.table.name')" align="center" />
        <el-table-column prop="link" :label="$t('propertyRightRegistration.table.link')" align="center" />
        <el-table-column prop="idCard" :label="$t('propertyRightRegistration.table.idCard')" align="center" />
        <el-table-column prop="address" :label="$t('propertyRightRegistration.table.address')" align="center" />
        <el-table-column prop="stateName" :label="$t('propertyRightRegistration.table.state')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="300">
          <template slot-scope="scope">
            <el-button v-if="scope.row.state !== '1'" size="mini" @click="handleExamine(scope.row)">
              {{ $t('common.examine') }}
            </el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button size="mini" type="info" @click="handleDetail(scope.row)">
              {{ $t('common.detail') }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 子组件 -->
    <add-property-right-registration ref="addForm" @success="handleSuccess" />
    <examine-property-right-registration ref="examineForm" @success="handleSuccess" />
    <edit-property-right-registration ref="editForm" @success="handleSuccess" />
    <delete-property-right-registration ref="deleteForm" @success="handleSuccess" />
  </div>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import { listPropertyRightRegistration } from '@/api/room/propertyRightRegistrationManageApi'
import { getFloors, getUnits } from '@/api/room/roomApi'
import AddPropertyRightRegistration from '@/components/room/addPropertyRightRegistration'
import ExaminePropertyRightRegistration from '@/components/room/examinePropertyRightRegistration'
import EditPropertyRightRegistration from '@/components/room/editPropertyRightRegistration'
import DeletePropertyRightRegistration from '@/components/room/deletePropertyRightRegistration'

export default {
  name: 'PropertyRightRegistrationManageList',
  components: {
    AddPropertyRightRegistration,
    ExaminePropertyRightRegistration,
    EditPropertyRightRegistration,
    DeletePropertyRightRegistration
  },
  data() {
    return {
      loading: false,
      showMoreCondition: false,
      searchForm: {
        roomId: '',
        allNum: '',
        name: '',
        link: '',
        idCard: '',
        address: '',
        state: '',
        floorId: '',
        unitId: '',
        communityId: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      states: [],
      floors: [],
      units: []
    }
  },
  created() {
    this.searchForm.communityId = getCommunityId()
    this.getList()
    this.getDictData()
    this.getFloors()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          ...this.searchForm,
          page: this.pagination.current,
          row: this.pagination.size
        }

        // 处理allNum
        if (params.allNum && params.allNum.split('-').length === 3) {
          const [floorNum, unitNum, roomNum] = params.allNum.split('-')
          params.floorNum = floorNum.trim()
          params.unitNum = unitNum.trim()
          params.roomNum = roomNum.trim()
        } else {
          params.floorNum = ''
          params.unitNum = ''
          params.roomNum = ''
        }

        const { data, total } = await listPropertyRightRegistration(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        console.error('获取列表失败:', error)
        this.$message.error(this.$t('propertyRightRegistration.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async getDictData() {
      try {
        this.states = await getDict('property_right_registration', 'state')
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async getFloors() {
      try {
        const params = {
          communityId: this.searchForm.communityId,
          page: 1,
          row: 50
        }
        const data = await getFloors(params)
        this.floors = data.apiFloorDataVoList || []
      } catch (error) {
        console.error('获取楼栋数据失败:', error)
      }
    },
    async handleFloorChange(floorId) {
      try {
        const params = {
          floorId,
          communityId: this.searchForm.communityId,
          page: 1,
          row: 50
        }
        const data = await getUnits(params)
        this.units = data || []
        this.searchForm.unitId = ''
      } catch (error) {
        console.error('获取单元数据失败:', error)
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        roomId: '',
        allNum: '',
        name: '',
        link: '',
        idCard: '',
        address: '',
        state: '',
        floorId: '',
        unitId: '',
        communityId: getCommunityId()
      }
      this.units = []
      this.handleSearch()
    },
    toggleMoreCondition() {
      this.showMoreCondition = !this.showMoreCondition
    },
    handleAdd() {
      this.$refs.addForm.open()
    },
    handleExamine(row) {
      this.$refs.examineForm.open(row)
    },
    handleEdit(row) {
      this.$refs.editForm.open(row)
    },
    handleDetail(row) {
      this.$router.push({
        path: '/views/room/listPropertyRightRegistrationDetail',
        query: {
          prrId: row.prrId,
          floorNum: row.floorNum,
          unitNum: row.unitNum,
          roomNum: row.roomNum
        }
      })
    },
    handleDelete(row) {
      this.$refs.deleteForm.open(row)
    },
    handleSuccess() {
      this.getList()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.property-right-registration-container {
  padding: 10px;
  margin: 0;

  .search-wrapper {
    margin-bottom: 20px;

    .el-row {
      margin-bottom: 20px;
    }

    .mt-20 {
      margin-top: 20px;
    }
  }

  .list-wrapper {
    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>