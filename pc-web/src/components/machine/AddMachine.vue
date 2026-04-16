<template>
    <el-dialog
        :title="$t('layout.machineAdd')"
        :visible.sync="visible"
        width="500px"
        @close="handleClose"
    >
        <el-form ref="form" :model="form" :rules="rules" label-width="100px">
            <el-form-item :label="$t('layout.machineCode')" prop="machineCode">
                <el-input v-model="form.machineCode" />
            </el-form-item>
            <el-form-item :label="$t('layout.machineName')" prop="machineName">
                <el-input v-model="form.machineName" />
            </el-form-item>
            <el-form-item :label="$t('layout.machineIp')" prop="machineIp">
                <el-input v-model="form.machineIp" />
            </el-form-item>
            <el-form-item :label="$t('layout.machineMac')" prop="machineMac">
                <el-input v-model="form.machineMac" />
            </el-form-item>
            <el-form-item :label="$t('layout.machineType')" prop="machineType">
                <el-select v-model="form.machineType" style="width: 100%">
                    <el-option label="门禁" value="1" />
                    <el-option label="道闸" value="2" />
                    <el-option label="两轮充电桩" value="3" />
                    <el-option label="四轮充电桩" value="4" />
                    <el-option label="水电表" value="5" />
                    <el-option label="梯控" value="6" />
                </el-select>
            </el-form-item>
            <el-form-item :label="$t('layout.implBean')" prop="implBean">
                <el-select v-model="form.implBean" style="width: 100%">
                    <el-option label="松饼" value="A" />
                    <el-option label="厂拍" value="B" />
                    <el-option label="臻识" value="C" />
                </el-select>
            </el-form-item>
            <el-form-item :label="$t('layout.status')" prop="statusCd">
                <el-radio-group v-model="form.statusCd">
                    <el-radio label="0">{{ $t('layout.inUse') }}</el-radio>
                    <el-radio label="1">{{ $t('layout.invalid') }}</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item :label="$t('layout.remark')" prop="remark">
                <el-input type="textarea" v-model="form.remark" />
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="handleClose">{{ $t('layout.cancel') }}</el-button>
            <el-button type="primary" @click="handleSubmit">{{ $t('layout.confirm') }}</el-button>
        </div>
    </el-dialog>
</template>

<script>
import { addMachine } from '@/api/machine/machineApi'

export default {
    name: 'AddMachine',
    props: {
        visible: {
            type: Boolean,
            default: false
        }
    },
    data() {
        return {
            form: {
                machineCode: '',
                machineName: '',
                machineIp: '',
                machineMac: '',
                implBean: '',
                statusCd: '0',
                remark: '',
                projectId: '',
                machineType: ''
            },
            rules: {
                machineCode: [
                    { required: true, message: this.$t('layout.required'), trigger: 'blur' }
                ],
                machineName: [
                    { required: true, message: this.$t('layout.required'), trigger: 'blur' }
                ],
                machineType: [
                    { required: true, message: this.$t('layout.required'), trigger: 'blur' }
                ],
                machineIp: [
                    { required: true, message: this.$t('layout.required'), trigger: 'blur' }
                ],
                implBean: [
                    { required: true, message: this.$t('layout.required'), trigger: 'change' }
                ]
            }
        }
    },
    methods: {
        initData(projectId) {
            this.form.projectId = projectId
        },
        handleClose() {
            this.$emit('update:visible', false)
            this.$refs.form.resetFields()
        },
        async handleSubmit() {
            try {
                await this.$refs.form.validate()
                await addMachine(this.form)
                this.$message.success(this.$t('common.operationSuccess'))
                this.$emit('success')
                this.handleClose()
            } catch (error) {
                if (error !== 'cancel') {
                    this.$message.error(this.$t('layout.submitError'))
                }
            }
        }
    }
}
</script>