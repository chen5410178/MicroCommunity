<template>
  <div class="advert-manage-container">
    <el-card class="search-wrapper">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('advertManage.search.title') }}</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="toggleMoreCondition">
          {{ advertManageInfo.moreCondition ? $t('advertManage.search.hide') : $t('advertManage.search.more') }}
        </el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="4">
          <el-input v-model="advertManageInfo.conditions.adName"
            :placeholder="$t('advertManage.search.adNamePlaceholder')" clearable />
        </el-col>
        <el-col :span="4">
          <el-select v-model="advertManageInfo.conditions.classify"
            :placeholder="$t('advertManage.search.classifyPlaceholder')" clearable>
            <el-option v-for="item in classifyOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="queryAdvert">{{ $t('common.search') }}</el-button>
          <el-button @click="resetAdvert">{{ $t('common.reset') }}</el-button>
        </el-col>
      </el-row>
      <el-row v-show="advertManageInfo.moreCondition" :gutter="20" style="margin-top: 15px;">
        <el-col :span="4">
          <el-select v-model="advertManageInfo.conditions.locationTypeCd"
            :placeholder="$t('advertManage.search.locationPlaceholder')" clearable>
            <el-option v-for="item in advertManageInfo.locationTypeCds" :key="item.statusCd" :label="item.name"
              :value="item.statusCd" />
          </el-select>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="list-wrapper">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('advertManage.list.title') }}</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="openAddAdvert">{{ $t('advertManage.list.publish') }}</el-button>
          <!-- <el-button type="primary" size="small" @click="viewAdvertPhoto">{{ $t('advertManage.list.viewAdvert')
          }}</el-button> -->
        </div>
      </div>

      <el-table v-loading="loading" :data="advertManageInfo.adverts" border style="width: 100%">
        <el-table-column prop="advertId" :label="$t('advertManage.table.advertId')" align="center" />
        <el-table-column prop="adName" :label="$t('advertManage.table.adName')" align="center" />
        <el-table-column prop="classifyName" :label="$t('advertManage.table.classify')" align="center" />
        <el-table-column prop="locationObjName" :label="$t('advertManage.table.location')" align="center" />
        <el-table-column :label="$t('advertManage.table.advertType')" align="center">
          <template slot-scope="scope">
            {{ advertManageInfo.advertTypeName[scope.row.advertType] }}
          </template>
        </el-table-column>
        <el-table-column prop="startTime" :label="$t('advertManage.table.startTime')" align="center" />
        <el-table-column prop="endTime" :label="$t('advertManage.table.endTime')" align="center" />
        <el-table-column prop="createTime" :label="$t('advertManage.table.createTime')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="openEditAdvert(scope.row)">{{ $t('common.edit') }}</el-button>
            <el-button size="mini" type="danger" @click="openDeleteAdvert(scope.row)">{{ $t('common.delete')
            }}</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <add-advert ref="addAdvert" @success="handleSuccess" />
    <edit-advert ref="editAdvert" @success="handleSuccess" />
    <delete-advert ref="deleteAdvert" @success="handleSuccess" />
    <write-advert-machine ref="writeAdvertMachine" />
  </div>
</template>

<script>
import { listAdverts } from '@/api/admin/advertManageApi'
import AddAdvert from '@/components/admin/AddAdvert'
import EditAdvert from '@/components/admin/EditAdvert'
import DeleteAdvert from '@/components/admin/DeleteAdvert'
import WriteAdvertMachine from '@/components/admin/WriteAdvertMachine'

export default {
  name: 'AdvertManageList',
  components: {
    AddAdvert,
    EditAdvert,
    DeleteAdvert,
    WriteAdvertMachine
  },
  data() {
    return {
      loading: false,
      advertManageInfo: {
        advertTypeName: ['', this.$t('advertManage.advertType.site'), this.$t('advertManage.advertType.outside'), this.$t('advertManage.advertType.noJump')],
        adverts: [],
        moreCondition: false,
        locationTypeCds: [],
        conditions: {
          adName: '',
          classify: '',
          locationTypeCd: ''
        }
      },
      classifyOptions: [
        { value: '9001', label: this.$t('advertManage.classify.logistics') },
        { value: '9002', label: this.$t('advertManage.classify.catering') },
        { value: '9003', label: this.$t('advertManage.classify.travel') },
        { value: '9004', label: this.$t('advertManage.classify.hotel') },
        { value: '9005', label: this.$t('advertManage.classify.education') },
        { value: '9006', label: this.$t('advertManage.classify.internet') }
      ],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getList()
    this.getLocationTypes()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          ...this.advertManageInfo.conditions
        }
        const { data, total } = await listAdverts(params)
        this.advertManageInfo.adverts = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('advertManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async getLocationTypes() {
      try {
        // 这里应该调用获取位置类型的API
        // const data = await getLocationTypes()
        // this.advertManageInfo.locationTypeCds = data
      } catch (error) {
        this.$message.error(this.$t('advertManage.fetchLocationError'))
      }
    },
    queryAdvert() {
      this.page.current = 1
      this.getList()
    },
    resetAdvert() {
      this.advertManageInfo.conditions = {
        adName: '',
        classify: '',
        locationTypeCd: ''
      }
      this.getList()
    },
    toggleMoreCondition() {
      this.advertManageInfo.moreCondition = !this.advertManageInfo.moreCondition
    },
    openAddAdvert() {
      this.$refs.addAdvert.open()
    },
    openEditAdvert(row) {
      this.$refs.editAdvert.open(row)
    },
    openDeleteAdvert(row) {
      this.$refs.deleteAdvert.open(row)
    },
    viewAdvertPhoto() {
      this.$refs.writeAdvertMachine.open()
    },
    handleSuccess() {
      this.getList()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.advert-manage-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;
  }

  .list-wrapper {
    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>