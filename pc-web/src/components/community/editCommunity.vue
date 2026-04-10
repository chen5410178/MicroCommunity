<template>
  <el-dialog :title="$t('editCommunity.title')" :visible.sync="visible" width="60%" @close="refreshEditCommunityInfo">
    <el-form ref="form" :model="editCommunityInfo" label-width="120px">
      <el-form-item :label="$t('editCommunity.name')" prop="name" required>
        <el-input v-model="editCommunityInfo.name" :placeholder="$t('editCommunity.namePlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('editCommunity.address')" prop="address" required>
        <el-input v-model="editCommunityInfo.address" :placeholder="$t('editCommunity.addressPlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('editCommunity.nearbyLandmarks')" prop="nearbyLandmarks" required>
        <el-input v-model="editCommunityInfo.nearbyLandmarks"
          :placeholder="$t('editCommunity.nearbyLandmarksPlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('editCommunity.payFeeMonth')" prop="payFeeMonth" required>
        <el-input v-model="editCommunityInfo.payFeeMonth" :placeholder="$t('editCommunity.payFeeMonthPlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('editCommunity.feePrice')" prop="feePrice" required>
        <el-input v-model="editCommunityInfo.feePrice" :placeholder="$t('editCommunity.feePricePlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('editCommunity.tel')" prop="tel" required>
        <el-input v-model="editCommunityInfo.tel" :placeholder="$t('editCommunity.telPlaceholder')" />
      </el-form-item>
      <el-form-item v-for="(item, index) in editCommunityInfo.attrs" :key="index" :label="item.specName"
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
      <el-button type="primary" @click="editCommunity">{{ $t('common.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateCommunity } from '@/api/community/communityManageApi'
import { getAttrSpecList } from '@/api/dev/attrSpecApi'
import { getAttrValueList } from '@/api/dev/attrValueApi'

export default {
  name: 'EditCommunity',
  data() {
    return {
      visible: false,
      editCommunityInfo: {
        communityId: '',
        name: '',
        address: '',
        nearbyLandmarks: '',
        cityCode: '',
        mapX: '101.33',
        mapY: '101.33',
        payFeeMonth: 12,
        feePrice: 0,
        tel: '',
        attrs: []
      }
    }
  },

  methods: {
    async openModal(community) {
      console.log(community)
      this.refreshEditCommunityInfo()
      this.visible = true
      Object.assign(this.editCommunityInfo, community)
      await this._loadEditCommunityAttrSpec()

      if (!community.communityAttrDtos) {
        return;
      }
      const attrDtos = community.communityAttrDtos
      let attrs = this.editCommunityInfo.attrs
      console.log(attrs,attrDtos)

      attrDtos.forEach(item => {
        attrs.forEach(attrItem => {
          console.log(item.specCd, attrItem.specCd)
          if (item.specCd == attrItem.specCd) {
            attrItem.attrId = item.attrId
            attrItem.value = item.value
          }
        })

      })

    },
    editCommunity() {
      if (!this.validateForm()) {
        return
      }
      updateCommunity(this.editCommunityInfo).then(res => {
        console.log(res)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('listData')

      }).catch(error => {
        this.$message.error(error.message)
      })
    },
    validateForm() {
      if (!this.editCommunityInfo.name) {
        this.$message.error(this.$t('editCommunity.validate.name'))
        return false
      }
      if (!this.editCommunityInfo.address) {
        this.$message.error(this.$t('editCommunity.validate.address'))
        return false
      }
      if (!this.editCommunityInfo.nearbyLandmarks) {
        this.$message.error(this.$t('editCommunity.validate.nearbyLandmarks'))
        return false
      }
      if (!this.editCommunityInfo.communityId) {
        this.$message.error(this.$t('editCommunity.validate.communityId'))
        return false
      }
      return true
    },
    refreshEditCommunityInfo() {
      const attrs = this.editCommunityInfo.attrs
      this.editCommunityInfo = {
        communityId: '',
        name: '',
        address: '',
        nearbyLandmarks: '',
        cityCode: '',
        mapX: '101.33',
        mapY: '101.33',
        payFeeMonth: 12,
        feePrice: 0,
        tel: '',
        attrs: attrs
      }
    },
    async _loadEditCommunityAttrSpec() {
      this.editCommunityInfo.attrs = []
      const { data } = await getAttrSpecList({
        page: 1,
        row: 100,
        tableName: 'building_community_attr'
      })
      data.forEach(item => {
        item.value = ''
        item.values = []
        this._loadEditAttrValue(item.specCd, item.values)
        if (item.specShow == 'Y') {
          this.editCommunityInfo.attrs.push(item)
        }
      })

    },
    async _loadEditAttrValue(specCd, values) {
      const { data } = await getAttrValueList({ specCd, page: 1, row: 100 })

      data.forEach(item => {
        if (item.valueShow == 'Y') {
          values.push(item)
        }
      })

    }
  }
}
</script>