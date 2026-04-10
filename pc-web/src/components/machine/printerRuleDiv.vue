<template>
  <el-card class="box-card">
    <div class="button-group">
      <el-button type="text" size="small" @click="openAddModal">
        <i class="el-icon-plus"></i>{{ $t('common.add') }}
      </el-button>
      <el-button type="text" size="small" @click="openEditModal">
        <i class="el-icon-edit"></i>{{ $t('common.edit') }}
      </el-button>
      <el-button type="text" size="small" @click="openDeleteModal">
        <i class="el-icon-delete"></i>{{ $t('common.delete') }}
      </el-button>
    </div>

    <div class="vc-org margin-top">
      <el-tree :data="printerRules" :props="defaultProps" @node-click="handleNodeClick" highlight-current></el-tree>
    </div>

    <add-printer-rule ref="addPrinterRule" @success="getPrinterRules" />
    <edit-printer-rule ref="editPrinterRule" @success="getPrinterRules" />
    <delete-printer-rule ref="deletePrinterRule" @success="getPrinterRules" />
  </el-card>
</template>

<script>
import { listPrinterRules } from '@/api/machine/printerRuleApi'
import { getCommunityId } from '@/api/community/communityApi'
import AddPrinterRule from '@/components/machine/addPrinterRule'
import EditPrinterRule from '@/components/machine/editPrinterRule'
import DeletePrinterRule from '@/components/machine/deletePrinterRule'

export default {
  name: 'PrinterRuleDiv',
  props: {
    value: {
      type: Object,
      default: () => ({})
    }
  },
  components: {
    AddPrinterRule,
    EditPrinterRule,
    DeletePrinterRule
  },
  data() {
    return {
      printerRules: [],
      defaultProps: {
        children: 'children',
        label: 'ruleName'
      },
      curRule: {},
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.getPrinterRules()
  },
  methods: {
    async getPrinterRules() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }
        const { data } = await listPrinterRules(params)
        this.printerRules = data
        if (this.printerRules.length > 0) {
          this.handleNodeClick(this.printerRules[0])
        }
      } catch (error) {
        console.error('获取打印机规则失败:', error)
      }
    },
    handleNodeClick(data) {
      this.curRule = data
      this.$emit('switchRule', this.curRule)
    },
    openAddModal() {
      this.$refs.addPrinterRule.open()
    },
    openEditModal() {
      if (!this.curRule.ruleId) {
        this.$message.warning(this.$t('printerRule.selectRule'))
        return
      }
      this.$refs.editPrinterRule.open(this.curRule)

    },
    openDeleteModal() {
      if (!this.curRule.ruleId) {
        this.$message.warning(this.$t('printerRule.selectRule'))
        return
      }
      this.$refs.deletePrinterRule.open(this.curRule)
    },
    refreshList() {
      this.getPrinterRules()
    }
  }
}
</script>

<style scoped>
.button-group {
  margin-bottom: 15px;
}

.vc-org {
  max-height: 500px;
  overflow-y: auto;
}

.margin-top {
  margin-top: 15px;
}
</style>