<template>
  <el-dialog :title="$t('menuGroupCatalog.dialog.addTitle')" :visible.sync="dialogVisible" width="500px"
    @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item :label="$t('menuGroupCatalog.form.gId')" prop="gId">
        <el-select v-model="form.gId" style="width: 100%" placeholder="请选择">
          <el-option :label="item.name" :value="item.gId" v-for="(item, index) in groups" :key="index" />
        </el-select>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('menuGroupCatalog.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('menuGroupCatalog.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { addMenuGroupCatalog } from '@/api/dev/menuGroupCatalogApi'
import { getMenuGroupList } from '@/api/dev/menuGroupApi'



export default {
  name: 'AddMenuGroupCatalog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    caId: {
      type: String,
      default: ''
    },
    storeType: {
      type: String,
      default: ''
    }
  },
  created() {
    this.getList()
  },
  data() {
    return {
      dialogVisible: false,
      form: {
        gId: '',
        storeType: '',
        createTime: '',
      },
      groups: [],
      rules: {
        caId: [
          { required: true, message: this.$t('menuGroupCatalog.validate.caId'), trigger: 'blur' }
        ],
        gId: [
          { required: true, message: this.$t('menuGroupCatalog.validate.gId'), trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    visible: {
      handler(val) {
        this.dialogVisible = val
      },
      immediate: true
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: 1,
          row: 500,
          storeType: this.storeType
        }
        const { menuGroups } = await getMenuGroupList(params)
        this.groups = menuGroups
      } catch (error) {
        this.$message.error(this.$t('menuGroup.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleClose() {
      this.dialogVisible = false
      this.$refs.form && this.$refs.form.resetFields()
    },
    async handleSubmit() {
      try {
        await addMenuGroupCatalog({
          gId: this.form.gId,
          caId: this.caId,
          storeType: this.storeType,
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.handleClose()
        this.$emit('success')
      } catch (error) {
        console.error('添加失败:', error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}
</style>