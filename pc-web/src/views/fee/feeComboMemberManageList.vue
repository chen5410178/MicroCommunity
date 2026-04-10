<template>
  <div class="fee-combo-member-manage-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ feeComboMemberManageInfo.comboName }} {{ $t('feeComboMemberManage.title') }}</span>
        <div class="header-tools">
          <el-button type="primary" size="small" @click="_openAddFeeComboMemberModal">
            <i class="el-icon-plus"></i>
            {{ $t('common.add') }}
          </el-button>
          <el-button type="primary" size="small" @click="_goBack">
            {{ $t('feeComboMemberManage.back') }}
          </el-button>
        </div>
      </div>

      <el-table :data="feeComboMemberManageInfo.feeComboMembers" border style="width: 100%" v-loading="loading">
        <el-table-column prop="feeTypeCdName" :label="$t('feeComboMemberManage.feeType')" align="center" />
        <el-table-column prop="feeName" :label="$t('feeComboMemberManage.feeItem')" align="center" />
        <el-table-column prop="feeFlagName" :label="$t('feeComboMemberManage.feeFlag')" align="center" />
        <el-table-column :label="$t('feeComboMemberManage.paymentType')" align="center">
          <template slot-scope="scope">
            {{ scope.row.paymentCd == '1200' ? $t('feeComboMemberManage.prepay') : $t('feeComboMemberManage.postpay') }}
          </template>
        </el-table-column>
        <el-table-column prop="paymentCycle" :label="$t('feeComboMemberManage.paymentCycle')" align="center" />
        <el-table-column :label="$t('feeComboMemberManage.unitPrice')" align="center">
          <template slot-scope="scope">
            {{ scope.row.computingFormula == '2002' ? '-' : scope.row.squarePrice }}
          </template>
        </el-table-column>
        <el-table-column prop="additionalAmount" :label="$t('feeComboMemberManage.additionalFee')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="150">
          <template slot-scope="scope">
            <el-button type="danger" size="mini" @click="_openDeleteFeeComboMemberModel(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.current"
        :page-sizes="[10, 20, 30, 50]" :page-size="page.size" layout="total, sizes, prev, pager, next, jumper"
        :total="page.total" class="pagination" />
    </el-card>

    <add-fee-combo-member ref="addFeeComboMember" @success="handleSuccess" />
    <delete-fee-combo-member ref="deleteFeeComboMember" @success="handleSuccess" />
  </div>
</template>

<script>
import { listFeeComboMember } from '@/api/fee/feeComboMemberManageApi'
import AddFeeComboMember from '@/components/fee/addFeeComboMember'
import DeleteFeeComboMember from '@/components/fee/deleteFeeComboMember'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'FeeComboMemberManageList',
  components: {
    AddFeeComboMember,
    DeleteFeeComboMember
  },
  data() {
    return {
      loading: false,
      feeComboMemberManageInfo: {
        feeComboMembers: [],
        comboId: '',
        comboName: '',
        conditions: {
          comboId: '',
          comboName: '',
          communityId: ''
        }
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.feeComboMemberManageInfo.conditions.comboId = this.$route.query.comboId
    this.feeComboMemberManageInfo.comboName = this.$route.query.comboName
    this.feeComboMemberManageInfo.conditions.communityId = getCommunityId()
    this._listFeeComboMembers()
  },
  methods: {
    async _listFeeComboMembers() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          ...this.feeComboMemberManageInfo.conditions
        }
        const { data, total } = await listFeeComboMember(params)
        this.feeComboMemberManageInfo.feeComboMembers = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('feeComboMemberManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _openAddFeeComboMemberModal() {
      this.$refs.addFeeComboMember.open({
        comboId: this.feeComboMemberManageInfo.conditions.comboId
      })
    },
    _openDeleteFeeComboMemberModel(feeComboMember) {
      this.$refs.deleteFeeComboMember.open(feeComboMember)
    },
    _goBack() {
      this.$router.go(-1)
    },
    handleSuccess() {
      this._listFeeComboMembers()
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listFeeComboMembers()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listFeeComboMembers()
    }
  }
}
</script>

<style lang="scss" scoped>
.fee-combo-member-manage-container {
  padding: 20px;

  .clearfix {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .header-tools {
    display: flex;
    gap: 10px;
  }

  .pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>