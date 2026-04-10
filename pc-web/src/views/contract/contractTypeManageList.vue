<template>
  <div class="contract-type-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('contractTypeManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="conditions.typeName" :placeholder="$t('contractTypeManage.search.typeNamePlaceholder')"
            clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="conditions.audit" :placeholder="$t('contractTypeManage.search.auditPlaceholder')"
            style="width:100%">
            <el-option v-for="item in audits" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="_queryContractTypeMethod">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="_resetContractTypeMethod">
            <i class="el-icon-refresh"></i>
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 合同类型列表 -->
    <el-card class="list-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('contractTypeManage.list.title') }}</span>
        <div style="float: right;">
          <el-button v-if="hasPrivilege('502020061767630003')" type="primary" size="small" @click="_toAuditPage">
            <i class="el-icon-setting"></i>
            {{ $t('contractTypeManage.list.setAudit') }}
          </el-button>
          <el-button type="primary" size="small" @click="_openAddContractTypeModal">
            <i class="el-icon-plus"></i>
            {{ $t('common.add') }}
          </el-button>
        </div>
      </div>

      <el-table :data="contractTypes" border style="width: 100%">
        <el-table-column prop="typeName" :label="$t('contractTypeManage.table.typeName')" align="center" />
        <el-table-column prop="auditName" :label="$t('contractTypeManage.table.audit')" align="center" />
        <el-table-column prop="remark" :label="$t('contractTypeManage.table.remark')" align="center" />
        <el-table-column prop="createTime" :label="$t('contractTypeManage.table.createTime')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="300">
          <template slot-scope="scope">
            <el-button size="mini" @click="_openContractTypeSpecModel(scope.row)">
              {{ $t('contractTypeManage.table.specInfo') }}
            </el-button>
            <el-button size="mini" @click="_openContractTemplate(scope.row)">
              {{ $t('contractTypeManage.table.template') }}
            </el-button>
            <el-button size="mini" type="primary" @click="_openEditContractTypeModel(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="_openDeleteContractTypeModel(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 子组件 -->
    <add-template-view ref="addTemplateView" @success="handleSuccess" />
    <add-contract-type ref="addContractType" @success="handleSuccess" />
    <edit-contract-type ref="editContractType" @success="handleSuccess" />
    <delete-contract-type ref="deleteContractType" @success="handleSuccess" />
  </div>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import {
  queryContractType,
  //queryContractTypeTemplate,

} from '@/api/contract/contractTypeManageApi'
import AddTemplateView from '@/components/contract/addTemplateView'
import AddContractType from '@/components/contract/addContractType'
import EditContractType from '@/components/contract/editContractType'
import DeleteContractType from '@/components/contract/deleteContractType'

export default {
  name: 'ContractTypeManageList',
  components: {
    AddTemplateView,
    AddContractType,
    EditContractType,
    DeleteContractType
  },
  data() {
    return {
      componentShow: 'contractTypeList',
      contractTypes: [],
      contractTypeAttrs: [],
      contractTypeSpec: '',
      templatecontent: '',
      audits: [],
      conditions: {
        typeName: '',
        audit: '',
        contractTypeId: ''
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._initData()
  },
  methods: {
    async _initData() {
      try {
        const auditData = await getDict('contract_type', 'audit')
        this.audits = auditData
        this._listContractTypes(this.page.current, this.page.size)
      } catch (error) {
        console.error('初始化数据失败:', error)
      }
    },
    async _listContractTypes(page, size) {
      try {
        const params = {
          page,
          row: size,
          typeName: this.conditions.typeName.trim(),
          audit: this.conditions.audit.trim(),
          communityId: this.communityId
        }
        const { data, total } = await queryContractType(params)
        this.contractTypes = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('contractTypeManage.fetchError'))
      }
    },
    _queryContractTypeMethod() {
      this.page.current = 1
      this._listContractTypes(this.page.current, this.page.size)
    },
    _resetContractTypeMethod() {
      this.conditions = {
        typeName: '',
        audit: '',
        contractTypeId: ''
      }
      this._listContractTypes(this.page.current, this.page.size)
    },
    _openAddContractTypeModal() {

      this.$refs.addContractType.open()
    },
    _openEditContractTypeModel(row) {
      this.$refs.editContractType.open(row)
    },
    _openDeleteContractTypeModel(row) {
      this.$refs.deleteContractType.open(row)
    },
    _openContractTypeSpecModel(row) {
      this.$router.push(`/views/contract/contractTypeSpecManage?contractTypeId=${row.contractTypeId}`)
    },
    _toAuditPage() {
      this.$router.push('/pages/property/workflowManage?tab=流程管理')
    },

    _openContractTemplate(row) {
      this.componentShow = ''
      this.$refs.addTemplateView.open(row)

    },
    handleSizeChange(val) {
      this.page.size = val
      this._listContractTypes(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listContractTypes(this.page.current, this.page.size)
    },
    handleSuccess() {
      this._listContractTypes(this.page.current, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.contract-type-manage-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
  }

  .list-card {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>