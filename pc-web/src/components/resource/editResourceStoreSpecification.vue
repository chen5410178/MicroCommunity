<template>
  <el-dialog 
    :title="$t('resourceStoreSpecificationManage.editItemSpec')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose">
    <el-form 
      :model="form" 
      :rules="rules" 
      ref="formRef" 
      label-width="120px">
      <el-form-item 
        :label="$t('resourceStoreSpecificationManage.specName')" 
        prop="specName">
        <el-input 
          v-model="form.specName" 
          :placeholder="$t('resourceStoreSpecificationManage.specNamePlaceholder')">
        </el-input>
      </el-form-item>
      
      <el-form-item 
        :label="$t('resourceStoreSpecificationManage.itemType')" 
        prop="parentRstId"
        class="item-type-form-item">
        <el-col :span="11">
          <el-select 
            v-model="form.parentRstId" 
            :placeholder="$t('resourceStoreSpecificationManage.selectItemType')"
            @change="handleParentTypeChange"
            style="width:100%">
            <el-option 
              v-for="item in resourceStoreTypes"
              :key="item.rstId"
              :label="item.name"
              :value="item.rstId">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="11" :offset="2">
          <el-select 
            v-model="form.rstId" 
            :placeholder="$t('resourceStoreSpecificationManage.selectSubCategory')"
            style="width:100%">
            <el-option 
              v-for="item in sonResourceStoreTypes"
              :key="item.rstId"
              :label="item.name"
              :value="item.rstId">
            </el-option>
          </el-select>
        </el-col>
      </el-form-item>
      
      <el-form-item 
        :label="$t('resourceStoreSpecificationManage.description')" 
        prop="description">
        <el-input 
          type="textarea"
          v-model="form.description" 
          :placeholder="$t('resourceStoreSpecificationManage.descriptionPlaceholder')"
          :rows="3">
        </el-input>
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('resourceStoreSpecificationManage.cancel') }}
      </el-button>
      <el-button type="primary" @click="submitForm">
        {{ $t('resourceStoreSpecificationManage.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateResourceStoreSpecification, listResourceStoreTypes } from '@/api/resource/resourceStoreSpecificationManageApi'

export default {
  name: 'EditResourceStoreSpecification',
  data() {
    return {
      visible: false,
      form: {
        rssId: '',
        specName: '',
        parentRstId: '',
        rstId: '',
        description: ''
      },
      resourceStoreTypes: [],
      sonResourceStoreTypes: [],
      rules: {
        specName: [
          { required: true, message: this.$t('common.requiredField'), trigger: 'blur' },
          { max: 255, message: this.$t('common.maxLength', { num: 255 }), trigger: 'blur' }
        ],
        parentRstId: [
          { required: true, message: this.$t('common.requiredField'), trigger: 'change' }
        ],
        rstId: [
          { required: true, message: this.$t('common.requiredField'), trigger: 'change' }
        ],
        description: [
          { max: 512, message: this.$t('common.maxLength', { num: 512 }), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(row) {
      this.form = {
        rssId: row.rssId,
        specName: row.specName,
        parentRstId: row.parentRstId,
        rstId: row.rstId,
        description: row.description
      }
      this.visible = true
      this.getResourceStoreTypes()
      this.getResourceStoreSonTypes(row.parentRstId)
    },
    
    async getResourceStoreTypes() {
      try {
        const params = {
          page: 1,
          row: 100,
          parentId: '0'
        }
        
        const res = await listResourceStoreTypes(params)
        this.resourceStoreTypes = res.data
      } catch (error) {
        console.error('Failed to get item types:', error)
      }
    },
    
    async getResourceStoreSonTypes(parentRstId) {
      try {
        const params = {
          page: 1,
          row: 100,
          rstId: parentRstId,
          flag: "0"
        }
        
        const res = await listResourceStoreTypes(params)
        this.sonResourceStoreTypes = res.data
      } catch (error) {
        console.error('Failed to get sub item types:', error)
      }
    },
    
    handleParentTypeChange(val) {
      this.form.rstId = ''
      if (!val) {
        this.sonResourceStoreTypes = []
        return
      }
      this.getResourceStoreSonTypes(val)
    },
    
    submitForm() {
      this.$refs.formRef.validate(async valid => {
        if (valid) {
          try {
            await updateResourceStoreSpecification(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('Failed to update specification:', error)
            this.$message.error(this.$t('common.updateFailed'))
          }
        }
      })
    },
    
    handleClose() {
      this.$refs.formRef.resetFields()
      this.form = {
        rssId: '',
        specName: '',
        parentRstId: '',
        rstId: '',
        description: ''
      }
      this.sonResourceStoreTypes = []
    }
  }
}
</script>

<style scoped>
.item-type-form-item {
  margin-bottom: 22px;
}
</style>