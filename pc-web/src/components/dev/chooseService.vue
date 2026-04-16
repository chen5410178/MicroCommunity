<template>
  <el-dialog 
    :title="$t('chooseService.title')" 
    :visible.sync="visible" 
    width="70%">
    <el-row :gutter="20">
      <el-col :span="18"></el-col>
      <el-col :span="6">
        <el-input 
          v-model="searchKey" 
          :placeholder="$t('chooseService.search')"
          clearable
          @keyup.enter.native="queryServices">
          <el-button 
            slot="append" 
            icon="el-icon-search" 
            @click="queryServices" />
        </el-input>
      </el-col>
    </el-row>
    <el-table :data="services" border style="width: 100%; margin-top: 20px">
      <el-table-column 
        prop="name" 
        :label="$t('chooseService.name')" 
        align="center" />
      <el-table-column 
        prop="serviceCode" 
        :label="$t('chooseService.serviceCode')" 
        align="center" />
      <el-table-column 
        prop="method" 
        :label="$t('chooseService.method')" 
        align="center" />
      <el-table-column 
        :label="$t('chooseService.operation')" 
        align="center" 
        width="120">
        <template slot-scope="scope">
          <el-button 
            type="primary" 
            size="mini" 
            @click="chooseService(scope.row)">
            {{ $t('chooseService.select') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>
</template>

<script>
import { listServices } from '@/api/dev/devServiceProvideApi'

export default {
  name: 'ChooseService',
  props: {
    emitChooseService: {
      type: String,
      default: ''
    },
    emitLoadData: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      visible: false,
      services: [],
      searchKey: '',
      page: {
        current: 1,
        size: 10
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.searchKey = ''
      this.loadServices()
    },
    loadServices() {
      listServices({
        page: this.page.current,
        row: this.page.size,
        name: this.searchKey
      }).then(response => {
        this.services = response.data
      }).catch(error => {
        this.$message.error(error.message)
      })
    },
    queryServices() {
      this.page.current = 1
      this.loadServices()
    },
    chooseService(service) {
      this.$emit(this.emitChooseService, service)
      this.$emit(this.emitLoadData, { serviceId: service.serviceId })
      this.visible = false
    }
  }
}
</script>