<template>
  <el-dialog :title="$t('addCommunity.title')" :visible.sync="visible" width="40%" @close="clearAddCommunityInfo">
    <el-form ref="form" :model="addCommunityInfo" :rules="rules" label-width="120px" class="text-left">
      <el-form-item :label="$t('addCommunity.name')" prop="name">
        <el-input v-model="addCommunityInfo.name" :placeholder="$t('addCommunity.name')" />
      </el-form-item>
      <el-form-item :label="$t('addCommunity.area')" prop="area">
        <area-select ref="areaSelectRef" @selectArea="selectArea" />
      </el-form-item>
      <el-form-item :label="$t('addCommunity.address')" prop="tmpAddress">
        <el-input v-model="addCommunityInfo.tmpAddress" :placeholder="$t('addCommunity.address')" />
      </el-form-item>
      <el-form-item :label="$t('addCommunity.nearbyLandmarks')" prop="nearbyLandmarks">
        <el-input v-model="addCommunityInfo.nearbyLandmarks"
          :placeholder="$t('addCommunity.nearbyLandmarks')" />
      </el-form-item>
      <el-form-item :label="$t('addCommunity.tel')" prop="tel">
        <el-input v-model="addCommunityInfo.tel" :placeholder="$t('addCommunity.tel')" />
      </el-form-item>
      <el-form-item :label="$t('addCommunity.payFeeMonth')" prop="payFeeMonth">
        <el-input v-model="addCommunityInfo.payFeeMonth" :placeholder="$t('addCommunity.payFeeMonth')" />
      </el-form-item>
      <el-form-item :label="$t('addCommunity.feePrice')" prop="feePrice">
        <el-input v-model="addCommunityInfo.feePrice" :placeholder="$t('addCommunity.feePrice')" />
      </el-form-item>
      <el-form-item v-for="(item, index) in addCommunityInfo.attrs" :key="index" :label="item.specName"
        :prop="'attrs.' + index + '.value'">
        <el-input v-if="item.specType === '2233'" v-model="item.value" :placeholder="item.specHoldplace" />
        <el-select v-else-if="item.specType === '3344'" v-model="item.value" :placeholder="item.specHoldplace"
          style="width: 100%">
          <el-option v-for="value in item.values" :key="value.value" :label="value.valueName" :value="value.value" />
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="saveCommunityInfo">{{ $t('common.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveCommunity } from '@/api/community/communityManageApi'
import { getAttrSpecList } from '@/api/dev/attrSpecApi'
import { getAttrValueList } from '@/api/dev/attrValueApi'
import AreaSelect from '@/components/community/areaSelect'

export default {
  name: 'AddCommunity',
  data() {
    return {
      visible: false,
      rules: {
        name: [
          { required: true, message: this.$t('addCommunity.validate.name'), trigger: 'blur' }
        ],
        tmpAddress: [
          { required: true, message: this.$t('addCommunity.validate.address'), trigger: 'blur' }
        ],
        nearbyLandmarks: [
          { required: true, message: this.$t('addCommunity.validate.nearbyLandmarks'), trigger: 'blur' }
        ],
        tel: [
          { required: true, message: this.$t('addCommunity.validate.tel'), trigger: 'blur' }
        ]
      },
      addCommunityInfo: {
        name: '',
        address: '',
        tmpAddress: '',
        areaAddress: '',
        nearbyLandmarks: '',
        tel: '',
        cityCode: '',
        mapX: '101.33',
        mapY: '101.33',
        attrs: [],
        payFeeMonth: 12,
        feePrice: 0
      }
    }
  },
  components: { AreaSelect },
  methods: {
    openModal() {
      this._loadCommunityAttrSpec()
      setTimeout(() => {
        this.$refs.areaSelectRef.clearArea()
      }, 100)
      this.visible = true
    },
    selectArea(area) {
      this.addCommunityInfo.cityCode = area.selectArea
    },
    saveCommunityInfo() {
      this.addCommunityInfo.address = this.addCommunityInfo.areaAddress + this.addCommunityInfo.tmpAddress
      if (!this.validateForm()) {
        return
      }
      saveCommunity(this.addCommunityInfo).then(res => {
        console.log(res)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('listData')
      }).catch(error => {
        this.$message.error(error.message)
      })
    },
    validateForm() {
      if (!this.addCommunityInfo.name) {
        this.$message.error(this.$t('addCommunity.validate.name'))
        return false
      }
      if (!this.addCommunityInfo.cityCode) {
        this.$message.error(this.$t('addCommunity.validate.cityCode'))
        return false
      }
      if (!this.addCommunityInfo.address) {
        this.$message.error(this.$t('addCommunity.validate.address'))
        return false
      }
      if (!this.addCommunityInfo.nearbyLandmarks) {
        this.$message.error(this.$t('addCommunity.validate.nearbyLandmarks'))
        return false
      }
      if (!this.addCommunityInfo.tel) {
        this.$message.error(this.$t('addCommunity.validate.tel'))
        return false
      }
      return true
    },
    clearAddCommunityInfo() {
      this.addCommunityInfo = {
        name: '',
        address: '',
        tmpAddress: '',
        areaAddress: '',
        nearbyLandmarks: '',
        cityCode: '',
        mapX: '101.33',
        mapY: '101.33',
        attrs: [],
        payFeeMonth: 12,
        feePrice: 0
      }
    },
    async _loadCommunityAttrSpec() {
      this.addCommunityInfo.attrs = []
      const { data } = await getAttrSpecList({
        page: 1,
        row: 100,
        tableName: 'building_community_attr'
      })
      data.forEach(item => {
        item.value = ''
        if (item.specShow === 'Y') {
          item.values = []
          this._loadAttrValue(item.specCd, item.values)
          this.addCommunityInfo.attrs.push(item)
        }
      })
    },
    async _loadAttrValue(specCd, values) {
      const { data } = await getAttrValueList({ specCd, page: 1, row: 100 })
      data.forEach(item => {
        if (item.valueShow === 'Y') {
          values.push(item)
        }
      })

    }
  }
}
</script>