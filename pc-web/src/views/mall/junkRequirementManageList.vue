<template>
  <div class="junk-requirement-container">
    <!-- Search Conditions -->
    <el-card class="search-wrapper">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('junkRequirement.search.title') }}</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="_moreCondition">
          {{ junkRequirementManageInfo.moreCondition ? $t('common.hide') : $t('common.more') }}
        </el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-select v-model="junkRequirementManageInfo.conditions.classification"
            :placeholder="$t('junkRequirement.search.classification')" clearable class="search-item">
            <el-option v-for="item in classificationOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-input v-model="junkRequirementManageInfo.conditions.publishUserName"
            :placeholder="$t('junkRequirement.search.publishUserName')" clearable class="search-item" />
        </el-col>
        <el-col :span="6">
          <el-select v-model="junkRequirementManageInfo.conditions.publishUserLink"
            :placeholder="$t('junkRequirement.search.state')" clearable class="search-item">
            <el-option v-for="item in stateOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="_queryJunkRequirementMethod">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
      <el-row v-if="junkRequirementManageInfo.moreCondition" :gutter="20">
        <el-col :span="6">
          <el-input v-model="junkRequirementManageInfo.conditions.publishUserLink"
            :placeholder="$t('junkRequirement.search.publishUserLink')" clearable class="search-item" />
        </el-col>
      </el-row>
    </el-card>

    <!-- List Table -->
    <el-card class="list-wrapper">
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('junkRequirement.list.title') }}</span>
      </div>
      <el-table v-loading="loading" :data="junkRequirementManageInfo.junkRequirements" border style="width: 100%">
        <el-table-column prop="junkRequirementId" :label="$t('junkRequirement.table.junkRequirementId')" align="center" />
        <el-table-column prop="classification" :label="$t('junkRequirement.table.classification')" align="center" />
        <el-table-column prop="referencePrice" :label="$t('junkRequirement.table.referencePrice')" align="center" />
        <el-table-column prop="publishUserName" :label="$t('junkRequirement.table.publishUserName')" align="center" />
        <el-table-column prop="publishUserLink" :label="$t('junkRequirement.table.publishUserLink')" align="center" />
        <el-table-column prop="stateName" :label="$t('junkRequirement.table.state')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="_openEditJunkRequirementModel(scope.row)">
              {{ $t('common.review') }}
            </el-button>
            <el-button size="mini" type="danger" @click="_openDeleteJunkRequirementModel(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <edit-junk-requirement ref="editJunkRequirement" @success="handleSuccess" />
    <delete-junk-requirement ref="deleteJunkRequirement" @success="handleSuccess" />
  </div>
</template>

<script>
import {
  listJunkRequirements
} from '@/api/mall/junkRequirementManageApi'
import EditJunkRequirement from '@/components/mall/EditJunkRequirement'
import DeleteJunkRequirement from '@/components/mall/DeleteJunkRequirement'

export default {
  name: 'JunkRequirementManageList',
  components: {
    EditJunkRequirement,
    DeleteJunkRequirement
  },
  data() {
    return {
      loading: false,
      junkRequirementManageInfo: {
        junkRequirements: [],
        moreCondition: false,
        pageName: 'Junk',
        conditions: {
          classification: '',
          publishUserName: '',
          publishUserLink: '',
          typeCd: '222222'
        }
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      classificationOptions: [
        { value: '10001', label: this.$t('junkRequirement.classification.furniture') },
        { value: '10002', label: this.$t('junkRequirement.classification.appliance') }
      ],
      stateOptions: [
        { value: '12001', label: this.$t('junkRequirement.state.unreviewed') },
        { value: '13001', label: this.$t('junkRequirement.state.reviewed') },
        { value: '14001', label: this.$t('junkRequirement.state.reviewFailed') },
        { value: '15001', label: this.$t('junkRequirement.state.completed') }
      ]
    }
  },
  created() {
    this._listJunkRequirements(this.page.current, this.page.size)
    this.junkRequirementManageInfo.conditions.typeCd = this.$route.query.typeCd
    if (this.$route.query.typeCd === '333333') {
      this.junkRequirementManageInfo.pageName = 'Requirement'
    } else {
      this.junkRequirementManageInfo.pageName = 'Junk'
    }
  },
  methods: {
    async _listJunkRequirements(page, size) {
      try {
        this.loading = true
        const params = {
          page: page,
          row: size,
          ...this.junkRequirementManageInfo.conditions,
          communityId: this.getCommunityId()
        }
        const { data, total } = await listJunkRequirements(params)
        this.junkRequirementManageInfo.junkRequirements = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('junkRequirement.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _openEditJunkRequirementModel(row) {
      this.$refs.editJunkRequirement.open(row)
    },
    _openDeleteJunkRequirementModel(row) {
      this.$refs.deleteJunkRequirement.open(row)
    },
    _queryJunkRequirementMethod() {
      this.page.current = 1
      this._listJunkRequirements(this.page.current, this.page.size)
    },
    _moreCondition() {
      this.junkRequirementManageInfo.moreCondition = !this.junkRequirementManageInfo.moreCondition
    },
    handleSuccess() {
      this._listJunkRequirements(this.page.current, this.page.size)
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listJunkRequirements(this.page.current, val)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listJunkRequirements(val, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.junk-requirement-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .search-item {
      width: 100%;
    }
  }

  .list-wrapper {
    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>