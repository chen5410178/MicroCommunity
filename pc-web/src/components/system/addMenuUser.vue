<template>
  <el-dialog :title="$t('menuUserManage.add.title')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('menuUserManage.add.menu')" prop="mId">
        <el-select v-model="form.mId" :placeholder="$t('menuUserManage.add.menuPlaceholder')" style="width:100%">
          <el-option v-for="item in menus" :key="item.mId" :label="`${item.menuGroupName}-${item.name}`"
            :value="item.mId" :disabled="item.isShow !== 'Y'" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('menuUserManage.add.icon')" prop="icon">
        <el-radio-group v-model="form.icon">
          <el-radio label="fa fa-adjust">
            <i class="fa fa-adjust"></i>
          </el-radio>
          <el-radio label="fa fa-bar-chart">
            <i class="fa fa-bar-chart"></i>
          </el-radio>
          <el-radio label="fa fa-comment-o">
            <i class="fa fa-comment-o"></i>
          </el-radio>
          <el-radio label="fa fa-folder-o">
            <i class="fa fa-folder-o"></i>
          </el-radio>
          <el-radio label="fa fa-futbol-o">
            <i class="fa fa-futbol-o"></i>
          </el-radio>
          <el-radio label="fa fa-globe">
            <i class="fa fa-globe"></i>
          </el-radio>
          <el-radio label="fa fa-female">
            <i class="fa fa-female"></i>
          </el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item :label="$t('menuUserManage.add.seq')" prop="seq">
        <el-input-number v-model="form.seq" :placeholder="$t('menuUserManage.add.seqPlaceholder')" :min="1"
          style="width:100%" />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveMenuUser,listCatalogMenus } from '@/api/system/menuUserManageApi'

export default {
  name: 'AddMenuUser',
  data() {
    return {
      visible: false,
      form: {
        mId: '',
        icon: 'fa fa-adjust',
        seq: 1
      },
      menus: [],
      rules: {
        mId: [
          { required: true, message: this.$t('menuUserManage.validate.menuRequired'), trigger: 'blur' }
        ],
        icon: [
          { required: true, message: this.$t('menuUserManage.validate.iconRequired'), trigger: 'blur' }
        ],
        seq: [
          { required: true, message: this.$t('menuUserManage.validate.seqRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.loadMenus()
    },
    async loadMenus() {
      try {
        const { data } = await listCatalogMenus()
        const newMenus = []
        data.forEach(item => {
          item.childs.forEach(child => {
            child.menuGroupName = item.name
            newMenus.push(child)
          })
        })
        this.menus = newMenus
      } catch (error) {
        console.error('Failed to load menus:', error)
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await saveMenuUser(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.$emit('success')
            this.visible = false
          } catch (error) {
            this.$message.error(error.message || this.$t('menuUserManage.add.error'))
          }
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.form.icon = 'fa fa-adjust'
      this.form.seq = 1
    }
  }
}
</script>

<style scoped>
.el-radio-group {
  display: flex;
  flex-wrap: wrap;
}

.el-radio {
  margin-right: 15px;
  margin-bottom: 10px;
}
</style>