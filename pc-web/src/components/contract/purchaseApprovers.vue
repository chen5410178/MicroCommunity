<template>
  <el-card class="box-card">
    <div slot="header" class="flex justify-between">
      <span>{{ $t('purchaseApprovers.title') }}</span>
    </div>

    <div v-if="purchaseApproversInfo.orgName">
      <el-form label-width="120px">
        <el-form-item :label="$t('purchaseApprovers.orgName')">
          <el-input v-model="purchaseApproversInfo.orgName" :placeholder="$t('purchaseApprovers.orgPlaceholder')"
            readonly></el-input>
        </el-form-item>

        <el-form-item :label="$t('purchaseApprovers.staffName')">
          <el-input v-model="purchaseApproversInfo.staffName" :placeholder="$t('purchaseApprovers.staffPlaceholder')"
            readonly></el-input>
        </el-form-item>
      </el-form>
    </div>

    <div v-else>
      <el-form label-width="120px">
        <el-form-item :label="$t('purchaseApprovers.staffName')">
          <el-col :span="18">
            <el-input v-model="purchaseApproversInfo.staffName" :placeholder="$t('purchaseApprovers.staffPlaceholder')"
              disabled></el-input>
          </el-col>
          <el-col :span="6">
            <el-button type="primary" @click="chooseStaff">
              <i class="el-icon-search"></i>
              {{ $t('common.select') }}
            </el-button>
          </el-col>
        </el-form-item>
      </el-form>
    </div>
    <select-staff ref="selectStaff" @selectStaff="handleSelectStaff" />

  </el-card>
</template>

<script>
import { getFirstStaff } from '@/api/contract/contractChangeDetailApi'
import { getCommunityId } from '@/api/community/communityApi'
import SelectStaff from '@/components/staff/SelectStaff'

export default {
  name: 'PurchaseApprovers',
  props: {
    flowType: {
      type: String,
      default: '60006'
    }
  },
  components: {
    SelectStaff
  },
  data() {
    return {
      purchaseApproversInfo: {
        flowComponent: 'purchaseApprovers',
        staffId: '',
        staffName: '',
        orgName: '',
        companyId: ''
      },
      communityId: ''
    }
  },
  watch: {
    purchaseApproversInfo: {
      deep: true,
      handler(newVal) {
        console.log('purchaseApproversInfo:', newVal)
        this.savePurchaseApprovers()
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.loadStaffOrg()
  },
  methods: {
    loadStaffOrg() {
      const param = {
        communityId: this.communityId,
        flowType: this.flowType
      }

      getFirstStaff(param)
        .then(response => {
          if (response.code === 0) {
            const data = response.data
            Object.assign(this.purchaseApproversInfo, data)
            this.purchaseApproversInfo.orgName = data.orgName
          }
        })
        .catch(error => {
          console.error('请求失败:', error)
        })
    },
    purchaseApproversValidate() {
      if (!this.purchaseApproversInfo.staffId) {
        this.$message.error(this.$t('purchaseApprovers.staffRequired'))
        return false
      }
      if (!this.purchaseApproversInfo.staffName) {
        this.$message.error(this.$t('purchaseApprovers.staffNameRequired'))
        return false
      }
      return true
    },
    savePurchaseApprovers() {
      if (this.purchaseApproversValidate()) {
        this.$emit(this.callBackListener, this.callBackFunction, this.purchaseApproversInfo)
      }
    },
    handleSelectStaff(staff) {
      this.purchaseApproversInfo.staffId = staff.userId
      this.purchaseApproversInfo.staffName = staff.userName
      this.$emit('notify3', {
        staffId: staff.userId,
        staffName: staff.userName
      })

    },
    chooseStaff() {
      this.$refs.selectStaff.open(this.purchaseApproversInfo)

    }
  }
}
</script>

<style scoped>
.box-card {
  margin-bottom: 20px;
}
</style>