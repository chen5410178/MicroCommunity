<template>
  <el-dialog :title="$t('chooseResourceStore.title')" :visible.sync="visible" width="80%" :close-on-click-modal="false">
    <el-row :gutter="20">
      <el-col :span="4">
        <el-select v-model="form.shId" :disabled="true" style="width:100%">
          <el-option :value="''" :label="$t('chooseResourceStore.selectWarehouse')"></el-option>
          <el-option v-for="(item, index) in storehouses" :key="index" :label="item.shName"
            :value="item.shId"></el-option>
        </el-select>
      </el-col>

      <el-col :span="4">
        <el-select v-model="form.parentRstId" @change="listResourceStoreSonTypes" style="width:100%">
          <el-option :value="''" :label="$t('chooseResourceStore.selectItemType')"></el-option>
          <el-option v-for="(item, index) in resourceStoreTypes" :key="index" :label="item.name"
            :value="item.rstId"></el-option>
        </el-select>
      </el-col>
      <el-col :span="4">
        <el-input v-model="form._currentResourceStoreName" :placeholder="$t('chooseResourceStore.inputItemName')"
          style="width:100%"></el-input>
      </el-col>
      <el-col :span="4">
        <el-select v-model="form.rstId" style="width:100%">
          <el-option :value="''" :label="$t('chooseResourceStore.selectSubType')"></el-option>
          <el-option v-for="(item, index) in resourceStoreSonTypes" :key="index" :label="item.name"
            :value="item.rstId"></el-option>
        </el-select>
      </el-col>
      <el-col :span="4">
        <el-button type="primary" @click="queryResourceStores" style="margin-left:10px">
          <i class="el-icon-search"></i>
          {{ $t('common.search') }}
        </el-button>
        <el-button @click="resetResourceStores">
          <i class="el-icon-refresh"></i>
          {{ $t('common.reset') }}
        </el-button>
      </el-col>
    </el-row>

    <div style="margin-top:15px">
      <el-table :data="resourceStores" border style="width:100%">
        <el-table-column width="50">
          <template slot-scope="scope">
            <el-checkbox v-model="selectResourceStores" :label="scope.row.resId"></el-checkbox>
          </template>
        </el-table-column>

        <el-table-column prop="shName" :label="$t('chooseResourceStore.warehouseName')" align="center"></el-table-column>

        <el-table-column prop="type" :label="$t('chooseResourceStore.itemType')" align="center">
          <template slot-scope="scope">
            {{ scope.row.parentRstName || '-' }} > {{ scope.row.rstName || '-' }}
          </template>
        </el-table-column>

        <el-table-column prop="resName" :label="$t('chooseResourceStore.itemName')" align="center"></el-table-column>

        <el-table-column prop="rssName" :label="$t('chooseResourceStore.itemSpec')" align="center">
          <template slot-scope="scope">
            {{ scope.row.rssName || '-' }}
          </template>
        </el-table-column>

        <el-table-column prop="resCode" :label="$t('chooseResourceStore.itemCode')" align="center"></el-table-column>

        <el-table-column prop="isFixedName" :label="$t('chooseResourceStore.isFixedItem')"
          align="center"></el-table-column>

        <el-table-column prop="stock" :label="$t('chooseResourceStore.itemStock')" align="center">
          <template slot-scope="scope">
            {{ scope.row.stock }}{{ scope.row.unitCodeName }}
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" style="margin-top:20px"></el-pagination>

      <div style="margin-top:20px;text-align:right">
        <el-button type="primary" @click="submitChoose">
          <i class="el-icon-check"></i>
          {{ $t('common.submit') }}
        </el-button>
        <el-button @click="visible = false">
          <i class="el-icon-close"></i>
          {{ $t('common.cancel') }}
        </el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import {
  listResourceStores,
  listStorehouses,
  listResourceStoreTypes
} from '@/api/resource/allocationStorehouseApplyApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ChooseResourceStore',
  props: {
    emitChooseResourceStore: String,
    emitLoadData: String
  },
  data() {
    return {
      visible: false,
      form: {
        resourceStores: [],
        selectResourceStores: [],
        _currentResourceStoreName: '',
        parentRstId: '',
        rstId: '',
        shId: '',
        newShId: '',
        storehouses: [],
        resourceStoreTypes: [],
        resourceStoreSonTypes: [],
        name: '',
        resOrderType: '',
        isAllocation: false
      },
      resourceStores: [],
      storehouses: [],
      resourceStoreTypes: [],
      resourceStoreSonTypes: [],
      selectResourceStores: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  watch: {
    selectResourceStores(newVal) {
      if (newVal.length === this.resourceStores.length) {
        this.$nextTick(() => {
          document.querySelector('#quan').checked = true
        })
      } else {
        document.querySelector('#quan').checked = false
      }
    }
  },
  methods: {
    open(params) {
      this.form = {
        ...this.form,
        resOrderType: params.resOrderType,
        shId: params.shId,
        newShId: params.shId,
        isAllocation: params.isAllocation
      }
      this.visible = true
      this.listStorehouses()
      this.listResourceStoreTypes()
      this.loadResourceStores(1, 10, '')
    },
    async listStorehouses() {
      try {
        const params = {
          page: 1,
          row: 100,
          flag: "1",
          communityId: getCommunityId()
        }
        const res = await listStorehouses(params)
        this.storehouses = res.data
        this.form.storehouses = res.data
      } catch (error) {
        console.error(error)
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
        const res = await listResourceStoreTypes(params)
        this.resourceStoreTypes = res.data
      } catch (error) {
        console.error(error)
      }
    },
    async listResourceStoreSonTypes() {
      this.form.rstId = ''
      if (!this.form.parentRstId) return

      try {
        const params = {
          page: 1,
          row: 100,
          communityId: getCommunityId(),
          parentId: this.form.parentRstId
        }
        const res = await listResourceStoreTypes(params)
        this.resourceStoreSonTypes = res.data
      } catch (error) {
        console.error(error)
      }
    },
    async loadResourceStores(page, size, name) {
      console.log(name)
      try {
        const params = {
          page,
          row: size,
          isShow: true,
          communityId: getCommunityId(),
          resOrderType: this.form.resOrderType,
          resName: this.form._currentResourceStoreName,
          parentRstId: this.form.parentRstId,
          rstId: this.form.rstId,
          shId: this.form.shId
        }

        if (this.form.shId) {
          params.communityId = ''
        }

        const res = await listResourceStores(params)
        this.resourceStores = res.resourceStores
        this.page.total = res.total
      } catch (error) {
        console.error(error)
      }
    },
    queryResourceStores() {
      this.loadResourceStores(1, 10, this.form._currentResourceStoreName)
    },
    resetResourceStores() {
      this.form.shId = this.form.newShId
      this.form.parentRstId = ""
      this.form.rstId = ""
      this.form._currentResourceStoreName = ""
      this.loadResourceStores(1, 10, '')
    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadResourceStores(this.page.current, val, '')
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadResourceStores(val, this.page.size, '')
    },
    submitChoose() {
      if (this.selectResourceStores.length < 1) {
        this.$message.warning(this.$t('chooseResourceStore.selectItemFirst'))
        return
      }

      const selectedItems = this.resourceStores.filter(item =>
        this.selectResourceStores.includes(item.resId)
      )

      this.$emit('choose', selectedItems)
      this.visible = false
      this.selectResourceStores = []
    },
    removeSelectedItem(resId) {
      this.selectResourceStores = this.selectResourceStores.filter(id => id !== resId)
    }
  }
}
</script>

<style scoped>
.el-checkbox {
  margin-left: 10px;
}
</style>