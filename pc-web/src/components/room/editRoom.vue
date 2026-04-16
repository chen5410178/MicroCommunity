<template>
  <el-dialog :title="$t('room.editRoom.title')" :visible.sync="dialogVisible" width="50%">
    <el-form :model="form" label-width="120px" label-position="right">
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('room.editRoom.roomNum')" required>
            <el-input v-model="form.roomNum" :placeholder="$t('room.editRoom.placeholder.roomNum')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('room.editRoom.layer')" required>
            <el-input v-model="form.layer" :placeholder="$t('room.editRoom.placeholder.layer')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('room.editRoom.unit')" required>
            <el-select v-model="form.unitId" :placeholder="$t('room.editRoom.placeholder.unit')" style="width: 100%;">
              <el-option v-for="unit in units" :key="unit.unitId" :label="`${unit.unitNum}`"
                :value="unit.unitId" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('room.editRoom.roomType')" required>
            <el-select v-model="form.roomSubType" :placeholder="$t('room.editRoom.placeholder.roomType')"
              style="width: 100%;">
              <el-option v-for="item in roomSubTypes" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('room.editRoom.apartment1')" required>
            <el-select v-model="form.apartment1" :placeholder="$t('room.editRoom.placeholder.apartment')"
              style="width: 100%;">
              <el-option v-for="item in apartment1Options" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('room.editRoom.apartment2')" required>
            <el-select v-model="form.apartment2" :placeholder="$t('room.editRoom.placeholder.apartment')"
              style="width: 100%;">
              <el-option v-for="item in apartment2Options" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row> -->

      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('room.editRoom.builtUpArea')" required>
            <el-input v-model="form.builtUpArea" type="number"
              :placeholder="$t('room.editRoom.placeholder.builtUpArea')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('room.editRoom.roomArea')" required>
            <el-input v-model="form.roomArea" :placeholder="$t('room.editRoom.placeholder.roomArea')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('room.editRoom.feeCoefficient')">
            <el-input v-model="form.feeCoefficient" :placeholder="$t('room.editRoom.placeholder.feeCoefficient')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('room.editRoom.roomRent')" required>
            <el-input v-model="form.roomRent" :placeholder="$t('room.editRoom.placeholder.roomRent')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row v-if="form.state !== '2002'">
        <el-col :span="12">
          <el-form-item :label="$t('room.editRoom.roomState')" required>
            <el-select v-model="form.state" :placeholder="$t('room.editRoom.placeholder.roomState')"
              style="width: 100%;">
              <el-option v-for="item in roomStateOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('room.editRoom.endTime')" required>
            <el-date-picker v-model="form.endTime" type="date" :placeholder="$t('room.editRoom.placeholder.endTime')"
              style="width: 100%;" value-format="yyyy-MM-dd" />
          </el-form-item>
        </el-col>
      </el-row>

      <div v-for="(item, index) in form.attrs" :key="index">
        <el-form-item :label="item.specName">
          <el-input v-if="item.specType === '2233'" v-model="item.value" :placeholder="item.specHoldplace" />
          <el-select v-else-if="item.specType === '3344'" v-model="item.value" :placeholder="item.specHoldplace">
            <el-option v-for="value in item.values" :key="value.value" :label="value.valueName" :value="value.value" />
          </el-select>
        </el-form-item>
      </div>

      <el-form-item :label="$t('room.editRoom.remark')">
        <el-input v-model="form.remark" type="textarea" :placeholder="$t('room.editRoom.placeholder.remark')"
          :rows="3" />
      </el-form-item>
    </el-form>

    <div slot="footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="editRoom">{{ $t('common.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateRoom, getUnits } from '@/api/room/editRoomApi'
import { getDict } from '@/api/community/communityApi'

export default {
  name: 'EditRoom',
  data() {
    return {
      dialogVisible: false,
      form: {
        roomId: '',
        unitId: '',
        roomNum: '',
        layer: '',
        apartment: '',
        apartment1: '',
        apartment2: '',
        builtUpArea: '',
        feeCoefficient: '1.00',
        state: '',
        remark: '',
        communityId: '',
        attrs: [],
        roomSubType: '110',
        roomArea: '',
        roomRent: '0',
        endTime: ''
      },
      units: [],
      roomSubTypes: [],
      apartment1Options: [
        { value: '10', label: this.$t('room.editRoom.apartment1Options.oneRoom') },
        { value: '20', label: this.$t('room.editRoom.apartment1Options.twoRooms') },
        { value: '30', label: this.$t('room.editRoom.apartment1Options.threeRooms') },
        { value: '40', label: this.$t('room.editRoom.apartment1Options.fourRooms') },
        { value: '50', label: this.$t('room.editRoom.apartment1Options.fiveRooms') },
        { value: '60', label: this.$t('room.editRoom.apartment1Options.sixRooms') },
        { value: '70', label: this.$t('room.editRoom.apartment1Options.sevenRooms') },
        { value: '80', label: this.$t('room.editRoom.apartment1Options.eightRooms') }
      ],
      apartment2Options: [
        { value: '101', label: this.$t('room.editRoom.apartment2Options.oneHall') },
        { value: '102', label: this.$t('room.editRoom.apartment2Options.twoHalls') },
        { value: '103', label: this.$t('room.editRoom.apartment2Options.threeHalls') },
        { value: '104', label: this.$t('room.editRoom.apartment2Options.fourHalls') },
        { value: '105', label: this.$t('room.editRoom.apartment2Options.fiveHalls') },
        { value: '106', label: this.$t('room.editRoom.apartment2Options.sixHalls') },
        { value: '107', label: this.$t('room.editRoom.apartment2Options.sevenHalls') },
        { value: '108', label: this.$t('room.editRoom.apartment2Options.eightHalls') }
      ],
      roomStateOptions: [
        { value: '2001', label: this.$t('room.editRoom.roomStateOptions.occupied') },
        { value: '2003', label: this.$t('room.editRoom.roomStateOptions.delivered') },
        { value: '2005', label: this.$t('room.editRoom.roomStateOptions.decorated') },
        { value: '2004', label: this.$t('room.editRoom.roomStateOptions.vacant') },
        { value: '2009', label: this.$t('room.editRoom.roomStateOptions.decorating') }
      ]
    }
  },
  created() {
    this.loadRoomAttrSpec()
  },
  beforeDestroy() {
  },
  methods: {
    async open(room) {
      this.resetForm()
      this.form = { ...this.form, ...room }

      try {
        // 加载房屋类型字典
        const data = await getDict('building_room', 'room_sub_type')
        this.roomSubTypes = data

        // 加载单元列表
        await this.loadUnits(room.floorId)

        this.dialogVisible = true
      } catch (error) {
        console.error('打开编辑房屋失败', error)
      }
    },
    async loadUnits(floorId) {
      try {
        const response = await getUnits({
          floorId: floorId,
          communityId: this.getCommunityId()
        })
        this.units = response
      } catch (error) {
        console.error('加载单元列表失败', error)
      }
    },
    async loadRoomAttrSpec() {
      try {
        // 这里需要实现获取属性规格的逻辑
        // 简化处理，实际项目中需要调用API
        this.form.attrs = []
      } catch (error) {
        console.error('加载房屋属性失败', error)
      }
    },
    async editRoom() {
      try {
        this.form.apartment = this.form.apartment1 + this.form.apartment2
        this.form.communityId = this.getCommunityId()

        await updateRoom(this.form)

        this.dialogVisible = false
        this.$emit('handleRefreshRoom')
      } catch (error) {
        console.error('更新房屋失败', error)
        this.$message.error(error.message || this.$t('common.updateFailed'))
      }
    },
    resetForm() {
      this.form = {
        roomId: '',
        unitId: '',
        roomNum: '',
        layer: '',
        apartment: '',
        apartment1: '',
        apartment2: '',
        builtUpArea: '',
        feeCoefficient: '1.00',
        state: '',
        remark: '',
        communityId: '',
        attrs: [],
        roomSubType: '110',
        roomArea: '',
        roomRent: '0',
        endTime: ''
      }
    },
  }
}
</script>