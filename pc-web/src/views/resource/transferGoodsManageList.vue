<template>
  <div class="transfer-goods-manage-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('transferGoodsManage.recipient') }}</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">
          <i class="el-icon-close"></i>
          {{ $t('common.back') }}
        </el-button>
      </div>
      <el-form label-width="120px">
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('transferGoodsManage.recipient')">
              <el-col :span="8">
                <el-input v-model="transferGoodsManageInfo.endUserInfo.staffName"
                  :placeholder="$t('transferGoodsManage.requiredContact')"></el-input>
              </el-col>
              <el-col :span="4" :offset="1">
                <el-button type="primary" @click="chooseEndUser">
                  <i class="el-icon-search"></i>
                  {{ $t('transferGoodsManage.selectRecipient') }}
                </el-button>
              </el-col>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('transferGoodsManage.transferDescription')">
              <el-col :span="20">
                <el-input type="textarea" v-model="transferGoodsManageInfo.description"
                  :placeholder="$t('transferGoodsManage.requiredDescription')" rows="4"></el-input>
              </el-col>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-card class="box-card" style="margin-top: 20px">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('transferGoodsManage.goodsInfo') }}</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="openSelectResourceStaffInfoModel">
          <i class="el-icon-search"></i>
          {{ $t('transferGoodsManage.selectGoods') }}
        </el-button>
      </div>

      <el-table :data="transferGoodsManageInfo.resourceStores" border style="width: 100%">
        <el-table-column prop="resName" :label="$t('transferGoodsManage.goodsName')" align="center" />
        <el-table-column prop="resCode" :label="$t('transferGoodsManage.goodsCode')" align="center" />
        <el-table-column :label="$t('transferGoodsManage.goodsType')" align="center">
          <template slot-scope="scope">
            {{ scope.row.parentRstName || '-' }} >
            {{ scope.row.rstName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="isFixedName" :label="$t('transferGoodsManage.isFixedGoods')" align="center" />
        <el-table-column :label="$t('transferGoodsManage.goodsStock')" align="center">
          <template slot-scope="scope">
            {{ scope.row.stock }}{{ scope.row.unitCodeName }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('transferGoodsManage.miniStock')" align="center">
          <template slot-scope="scope">
            {{ scope.row.miniStock }}{{ scope.row.miniUnitCodeName }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('transferGoodsManage.transferQuantity')" align="center">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.giveQuantity" 
              controls-position="right" style="width: 120px"></el-input-number>
            {{ scope.row.miniUnitCodeName }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('transferGoodsManage.remark')" align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.purchaseRemark"></el-input>
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.operation')" align="center" width="120">
          <template slot-scope="scope">
            <el-button type="danger" size="mini" @click="removeSelectResourceStoreItem(scope.row.resId)">
              <i class="el-icon-delete"></i>
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <ChooseResourceStaff ref="chooseResourceStaff" @setSelectResourceStores="setSelectResourceStores" />
    </el-card>

    <el-row style="margin-top: 20px">
      <el-col :span="24" style="text-align: right">
        <el-button type="primary" @click="finishStep">
          {{ $t('common.submit') }}
        </el-button>
      </el-col>
    </el-row>

    <SelectStaff ref="selectStaff" @selectStaff="setEndUserInfo" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import ChooseResourceStaff from '@/components/resource/chooseResourceStaff'
import SelectStaff from '@/components/staff/SelectStaff'
import {getUserId} from '@/api/user/userApi'
import {saveAllocationUserStorehouse} from '@/api/resource/transferGoodsManageApi'

export default {
  name: 'TransferGoodsManageList',
  components: {
    ChooseResourceStaff,
    SelectStaff
  },
  data() {
    return {
      communityId: '',
      transferGoodsManageInfo: {
        description: '',
        endUserInfo: {
          staffId: '',
          staffName: '',
          staffTel: ''
        },
        resourceStores: []
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.initStep()
  },
  methods: {
    initStep() {
      this.transferGoodsManageInfo = {
        description: '',
        endUserInfo: {
          staffId: '',
          staffName: '',
          staffTel: ''
        },
        resourceStores: []
      }
    },
    goBack() {
      this.$router.go(-1)
    },
    chooseEndUser() {
      this.$refs.selectStaff.open()
    },
    setEndUserInfo(staff) {
      this.transferGoodsManageInfo.endUserInfo = {
        staffId: staff.userId,
        staffName: staff.userName,
        staffTel: staff.tel
      }
    },
    openSelectResourceStaffInfoModel() {
      this.$refs.chooseResourceStaff.open()
    },
    setSelectResourceStores(resourceStores) {
      // 保留用户之前输入的数量和备注
      const oldList = this.transferGoodsManageInfo.resourceStores
      resourceStores.forEach(newItem => {
        newItem.quantity = 0
        oldList.forEach(oldItem => {
          if (oldItem.resId === newItem.resId) {
            newItem.giveQuantity = oldItem.giveQuantity
            newItem.price = oldItem.price
            newItem.purchaseRemark = oldItem.purchaseRemark
          }
        })
      })
      this.transferGoodsManageInfo.resourceStores = resourceStores
    },
    removeSelectResourceStoreItem(resId) {
      this.transferGoodsManageInfo.resourceStores = this.transferGoodsManageInfo.resourceStores.filter(
        item => item.resId !== resId
      )
    },
    async finishStep() {
      const postData = {
        resourceStores: this.transferGoodsManageInfo.resourceStores,
        description: this.transferGoodsManageInfo.description,
        file: '',
        acceptUserId: this.transferGoodsManageInfo.endUserInfo.staffId,
        acceptUserName: this.transferGoodsManageInfo.endUserInfo.staffName,
        communityId: this.communityId
      }

      const currentUserId = getUserId()
      if (currentUserId === postData.acceptUserId) {
        this.$message.error(this.$t('transferGoodsManage.cannotTransferToSelf'))
        return
      }

      if (!postData.acceptUserId) {
        this.$message.error(this.$t('transferGoodsManage.selectRecipientFirst'))
        return
      }

      if (!postData.description) {
        this.$message.error(this.$t('transferGoodsManage.fillDescriptionFirst'))
        return
      }

      const _resourceStores = postData.resourceStores
      if (_resourceStores.length <= 0) {
        this.$message.error(this.$t('transferGoodsManage.selectGoodsFirst'))
        return
      }

      // 校验商品信息
      for (let i = 0; i < _resourceStores.length; i++) {
        if (
          !Object.prototype.hasOwnProperty.call(_resourceStores[i], 'giveQuantity') ||
          parseFloat(_resourceStores[i].giveQuantity) <= 0
        ) {
          this.$message.error(this.$t('transferGoodsManage.fillQuantityFirst'))
          return
        }
        _resourceStores[i].giveQuantity = parseFloat(_resourceStores[i].giveQuantity)
        if (
          _resourceStores[i].giveQuantity >
          parseFloat(_resourceStores[i].miniStock)
        ) {
          this.$message.error(
            `${_resourceStores[i].resName},${this.$t('transferGoodsManage.insufficientStock')}`
          )
          return
        }
      }

      try {
        const res = await saveAllocationUserStorehouse(
          postData
        )
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$router.push('/resource/myResourceStoreManage')
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('common.operateFailed'))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.transfer-goods-manage-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .clearfix {
    font-size: 18px;
    font-weight: bold;
  }

  .el-form-item {
    margin-bottom: 22px;
  }

  .el-input-number {
    width: 120px;
  }
}
</style>