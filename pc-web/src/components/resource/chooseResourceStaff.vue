<template>
  <el-dialog
    :title="$t('transferGoodsManage.selectGoods')"
    :visible.sync="visible"
    width="80%"
    @close="handleClose"
  >

      <el-row :gutter="20">
        <el-col :span="6">
          <el-select
            v-model="queryParams.parentRstId"
            :placeholder="$t('transferGoodsManage.selectGoodsType')"
            style="width: 100%"
            @change="listResourceStoreSonTypes"
          >
            <el-option
              v-for="item in resourceStoreTypes"
              :key="item.rstId"
              :label="item.name"
              :value="item.rstId"
            ></el-option>
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select
            v-model="queryParams.rstId"
            :placeholder="$t('transferGoodsManage.selectSubCategory')"
            style="width: 100%"
          >
            <el-option
              v-for="item in resourceStoreSonTypes"
              :key="item.rstId"
              :label="item.name"
              :value="item.rstId"
            ></el-option>
          </el-select>
        </el-col>
        <el-col :span="10">
          <el-input
            v-model="queryParams._currentResourceStoreName"
            :placeholder="$t('transferGoodsManage.inputGoodsName')"
            style="width: 100%"
          >
            <el-button
              slot="append"
              icon="el-icon-search"
              @click="queryResourceStores"
            ></el-button>
          </el-input>
        </el-col>
        <el-col :span="2">
          <el-button @click="resetResourceStores">
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>

      <el-table
        :data="resourceStores"
        style="width: 100%; margin-top: 20px"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column
          prop="parentRstName"
          :label="$t('transferGoodsManage.goodsType')"
          align="center"
        >
          <template slot-scope="scope">
            {{ scope.row.parentRstName || '-' }} >
            {{ scope.row.rstName || '-' }}
          </template>
        </el-table-column>
        <el-table-column
          prop="resName"
          :label="$t('transferGoodsManage.goodsName')"
          align="center"
        />
        <el-table-column
          prop="resCode"
          :label="$t('transferGoodsManage.goodsCode')"
          align="center"
        />
        <el-table-column
          prop="isFixedName"
          :label="$t('transferGoodsManage.isFixedGoods')"
          align="center"
        />
        <el-table-column
          :label="$t('transferGoodsManage.goodsStock')"
          align="center"
        >
          <template slot-scope="scope">
            {{ scope.row.stock }}{{ scope.row.unitCodeName }}
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('transferGoodsManage.miniStock')"
          align="center"
        >
          <template slot-scope="scope">
            {{ scope.row.miniStock }}{{ scope.row.miniUnitCodeName }}
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        :current-page="pagination.current"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="pagination.size"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        style="margin-top: 20px"
      ></el-pagination>


    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="submitSelected">
        {{ $t('common.submit') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import {listUserStorehouses,listResourceStoreTypes} from '@/api/resource/transferGoodsManageApi'

export default {
  name: 'ChooseResourceStaff',
  data() {
    return {
      visible: false,
      queryParams: {
        _currentResourceStoreName: '',
        parentRstId: '',
        rstId: '',
        giveType: 1
      },
      resourceStores: [],
      selectedResources: [],
      resourceStoreTypes: [],
      resourceStoreSonTypes: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.queryParams = {
        _currentResourceStoreName: '',
        parentRstId: '',
        rstId: '',
        giveType: 1
      }
      this.loadAllResourceStaffInfo()
      this.listResourceStoreTypes()
    },
    handleClose() {
      this.selectedResources = []
    },
    async loadAllResourceStaffInfo() {
      try {
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          communityId: getCommunityId(),
          resName: this.queryParams._currentResourceStoreName,
          parentRstId: this.queryParams.parentRstId,
          rstId: this.queryParams.rstId,
          giveType: this.queryParams.giveType
        }

        const res = await listUserStorehouses(
          params
        )
        this.resourceStores = res.data
        this.pagination.total = res.total
      } catch (error) {
        this.$message.error(this.$t('common.loadFailed'))
      }
    },
    async listResourceStoreTypes() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: getCommunityId(),
          parentId: '0'
        }

        const res = await listResourceStoreTypes(
          params
        )
        this.resourceStoreTypes = res.data
      } catch (error) {
        this.$message.error(this.$t('common.loadFailed'))
      }
    },
    async listResourceStoreSonTypes() {
      this.queryParams.rstId = ''
      this.resourceStoreSonTypes = []
      if (!this.queryParams.parentRstId) return

      try {
        const params = {
          page: 1,
          row: 100,
          communityId: getCommunityId(),
          parentId: this.queryParams.parentRstId
        }

        const res = await this.$api.resource.transferGoodsManageApi.listResourceStoreTypes(
          params
        )
        this.resourceStoreSonTypes = res.data
      } catch (error) {
        this.$message.error(this.$t('common.loadFailed'))
      }
    },
    queryResourceStores() {
      this.pagination.current = 1
      this.loadAllResourceStaffInfo()
    },
    resetResourceStores() {
      this.queryParams = {
        _currentResourceStoreName: '',
        parentRstId: '',
        rstId: '',
        giveType: 1
      }
      this.pagination.current = 1
      this.loadAllResourceStaffInfo()
    },
    handleSelectionChange(val) {
      this.selectedResources = val
    },
    submitSelected() {
      if (this.selectedResources.length === 0) {
        this.$message.warning(this.$t('transferGoodsManage.selectGoodsFirst'))
        return
      }
      this.$emit('setSelectResourceStores', this.selectedResources)
      this.visible = false
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadAllResourceStaffInfo()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadAllResourceStaffInfo()
    }
  }
}
</script>

<style scoped>
.el-select {
  width: 100%;
}
</style>