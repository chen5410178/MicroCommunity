<template>
  <div class="integral-detail-container">
    <el-row :gutter="20">
      <el-col :span="3">
        <el-card class="tree-card">
          <ul class="tree-list">
            <li class="tree-item" @click="_switchIntegralObjType('')"
              :class="{ 'active': integralDetailInfo.conditions.objType === '' }">
              {{ $t('integralDetail.all') }}
            </li>
            <li class="tree-item" @click="_switchIntegralObjType('6006')"
              :class="{ 'active': integralDetailInfo.conditions.objType === '6006' }">
              {{ $t('integralDetail.personal') }}
            </li>
            <li class="tree-item" @click="_switchIntegralObjType('7007')"
              :class="{ 'active': integralDetailInfo.conditions.objType === '7007' }">
              {{ $t('integralDetail.merchant') }}
            </li>
          </ul>
        </el-card>
      </el-col>
      <el-col :span="21">
        <el-card class="search-card">
          <div slot="header" class="clearfix text-left">
            <span>{{ $t('integralDetail.searchTitle') }}</span>
          </div>
          <el-form :inline="true" :model="integralDetailInfo.conditions" class="search-form text-left">
            <el-form-item>
              <el-input v-model="integralDetailInfo.conditions.integralNameLike"
                :placeholder="$t('integralDetail.accountPlaceholder')" clearable />
            </el-form-item>
            <el-form-item>
              <el-select v-model="integralDetailInfo.conditions.detailType"
                :placeholder="$t('integralDetail.typePlaceholder')">
                <el-option :label="$t('integralDetail.all')" value="" />
                <el-option :label="$t('integralDetail.transferIn')" value="1001" />
                <el-option :label="$t('integralDetail.transferOut')" value="2002" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-select v-model="integralDetailInfo.conditions.objType"
                :placeholder="$t('integralDetail.objectTypePlaceholder')">
                <el-option :label="$t('integralDetail.all')" value="" />
                <el-option :label="$t('integralDetail.user')" value="6006" />
                <el-option :label="$t('integralDetail.merchant')" value="7007" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-input v-model="integralDetailInfo.conditions.orderId"
                :placeholder="$t('integralDetail.orderIdPlaceholder')" clearable />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="_queryIntegralDetailMethod">
                <i class="el-icon-search"></i>
                {{ $t('common.search') }}
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card class="table-card">
          <div slot="header" class="clearfix text-left">
            <span>{{ $t('integralDetail.tableTitle') }}</span>
          </div>
          <el-table :data="integralDetailInfo.integralDetails" border style="width: 100%" v-loading="loading">
            <el-table-column prop="detailId" :label="$t('integralDetail.id')" align="center" width="80" />
            <el-table-column prop="integralName" :label="$t('integralDetail.accountName')" align="center" />
            <el-table-column prop="integralId" :label="$t('integralDetail.accountId')" align="center" />
            <el-table-column :label="$t('integralDetail.type')" align="center">
              <template slot-scope="scope">
                {{ scope.row.detailType === '2002' ? $t('integralDetail.transferOut') : $t('integralDetail.transferIn') }}
              </template>
            </el-table-column>
            <el-table-column prop="relIntegralId" :label="$t('integralDetail.relatedAccount')" align="center" />
            <el-table-column prop="amount" :label="$t('integralDetail.points')" align="center" />
            <el-table-column :label="$t('integralDetail.objectType')" align="center">
              <template slot-scope="scope">
                {{ scope.row.objType === '7007' ? $t('integralDetail.merchant') : $t('integralDetail.user') }}
              </template>
            </el-table-column>
            <el-table-column prop="objId" :label="$t('integralDetail.objectId')" align="center" />
            <el-table-column prop="link" :label="$t('integralDetail.phone')" align="center" />
            <el-table-column prop="orderId" :label="$t('integralDetail.orderId')" align="center" />
            <el-table-column prop="createTime" :label="$t('integralDetail.transactionTime')" align="center" />
            <el-table-column prop="remark" :label="$t('integralDetail.remark')" align="center" />
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            layout="total, sizes, prev, pager, next, jumper" :total="page.total" class="pagination">
          </el-pagination>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getIntegralDetailList } from '@/api/mall/integralDetailApi'

export default {
  name: 'IntegralDetailList',
  data() {
    return {
      loading: false,
      integralDetailInfo: {
        integralDetails: [],
        conditions: {
          integralId: '',
          detailType: '',
          integralNameLike: '',
          amount: '',
          objType: '',
          orderId: '',
          mallApiCode: 'queryIntegralDetailBmoImpl'
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
    this._listIntegralDetails(this.page.current, this.page.size)
  },
  methods: {
    async _listIntegralDetails(page, size) {
      try {
        this.loading = true
        this.integralDetailInfo.conditions.page = page
        this.integralDetailInfo.conditions.row = size

        const { data, total } = await getIntegralDetailList(this.integralDetailInfo.conditions)
        this.integralDetailInfo.integralDetails = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('integralDetail.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _queryIntegralDetailMethod() {
      this.page.current = 1
      this._listIntegralDetails(this.page.current, this.page.size)
    },
    _switchIntegralObjType(objType) {
      this.integralDetailInfo.conditions.objType = objType
      this._listIntegralDetails(this.page.current, this.page.size)
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listIntegralDetails(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listIntegralDetails(this.page.current, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.integral-detail-container {
  padding: 20px;

  .tree-card {
    height: 100%;

    .tree-list {
      list-style: none;
      padding: 0;
      margin: 0;

      .tree-item {
        padding: 10px;
        margin-bottom: 5px;
        text-align: center;
        cursor: pointer;
        border-radius: 4px;

        &:hover {
          background-color: #f5f7fa;
        }

        &.active {
          background-color: #409eff;
          color: white;
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;

    .search-form {
      .el-form-item {
        margin-bottom: 0;
      }
    }
  }

  .pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>