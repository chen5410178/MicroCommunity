<template>
    <el-dialog :title="isEdit ? $t('role.editRole') : $t('role.addRole')" :visible.sync="visible" width="40%"
        @close="resetForm">
        <el-form ref="form" :model="form" :rules="rules" label-width="120px">
            <el-alert v-if="errorInfo" :title="errorInfo" type="error" show-icon />

            <el-form-item :label="$t('role.name')" prop="name">
                <el-input v-model="form.name" :placeholder="$t('role.requiredName')" />
            </el-form-item>

            <el-form-item :label="$t('role.description')" prop="description">
                <el-input v-model="form.description" type="textarea" :placeholder="$t('role.descriptionLength')" :rows="4" />
            </el-form-item>
        </el-form>

        <div slot="footer" class="dialog-footer">
            <el-button @click="visible = false">{{ $t('role.cancel') }}</el-button>
            <el-button type="primary" @click="submitForm">{{ $t('role.save') }}</el-button>
        </div>
    </el-dialog>
</template>

<script>
import { savePrivilegeGroup, updatePrivilegeGroup } from '@/api/role/roleApi'

export default {
    name: 'AddPrivilegeGroup',
    data() {
        return {
            visible: false,
            isEdit: false,
            errorInfo: '',
            form: {
                pgId: '',
                name: '',
                description: ''
            },
            rules: {
                name: [
                    { required: true, message: this.$t('role.nameRequired'), trigger: 'blur' },
                    { min: 2, max: 10, message: this.$t('role.nameLength'), trigger: 'blur' }
                ],
                description: [
                    { max: 200, message: this.$t('role.descLength'), trigger: 'blur' }
                ]
            }
        }
    },
    methods: {
        open(data = null) {
            if (data) {
                this.isEdit = true
                this.form = { ...data }
            } else {
                this.isEdit = false
            }
            this.visible = true
        },
        resetForm() {
            this.$refs.form.resetFields()
            this.form = {
                pgId: '',
                name: '',
                description: ''
            }
            this.errorInfo = ''
        },
        async submitForm() {

            try {
                if (this.isEdit) {
                    await updatePrivilegeGroup(this.form)
                } else {
                    await savePrivilegeGroup(this.form)
                }

                this.$message.success(this.$t('common.operationSuccess'))
                this.visible = false
                this.$emit('success')
            } catch (error) {
                this.errorInfo = error.message || this.$t('role.saveError')
            }

        }
    }

}
</script>

<style lang="scss" scoped>
.el-alert {
    margin-bottom: 20px;
}
</style>