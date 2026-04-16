<template>
  <div class="add-room-view-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between ">
        <div>{{ $t('addRoomView.title') }}</div>
        <div class="">
          <el-button type="primary" size="small" @click="_goBack">
            <i class="el-icon-close"></i>{{ $t('common.back') }}
          </el-button>
        </div>
      </div>
      <el-form :model="addRoomViewInfo" label-position="right" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addRoomView.building')">
              <el-select v-model="addRoomViewInfo.floorId" @change="_loadUnit" placeholder="必填，请选择楼栋" style="width:100%">
                <el-option v-for="(floor, index) in addRoomViewInfo.floors" :key="index" :label="floor.floorName"
                  :value="floor.floorId"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addRoomView.unit')">
              <el-select v-model="addRoomViewInfo.unitId" placeholder="必填，请选择单元" style="width:100%">
                <template  v-for="(unit, index) in addRoomViewInfo.units">
                  <el-option v-if="unit.unitNum != '0'" :key="index" :label="unit.unitNum"
                    :value="unit.unitId"></el-option>
                </template>
              </el-select>

            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addRoomView.roomNumber')">
              <el-input v-model="addRoomViewInfo.roomNum" :placeholder="$t('addRoomView.requiredRoomNumber')"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addRoomView.roomFloor')">
              <el-input v-model="addRoomViewInfo.layer" :placeholder="$t('addRoomView.requiredRoomFloor')"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addRoomView.roomType')">
              <el-select v-model="addRoomViewInfo.roomSubType" placeholder="必填，请选择房屋类型" style="width:100%">
                <el-option v-for="(item, index) in addRoomViewInfo.roomSubTypes" :key="index" :label="item.name"
                  :value="item.statusCd"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="addRoomViewInfo.roomSubType != '110'">
            <el-form-item :label="$t('addRoomView.rent')">
              <el-input v-model="addRoomViewInfo.roomRent" :placeholder="$t('addRoomView.requiredRent')"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addRoomView.builtUpArea')">
              <el-input v-model="addRoomViewInfo.builtUpArea"
                :placeholder="$t('addRoomView.requiredBuiltUpArea')"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addRoomView.roomArea')">
              <el-input v-model="addRoomViewInfo.roomArea" :placeholder="$t('addRoomView.requiredRoomArea')"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addRoomView.feeCoefficient')">
              <el-input v-model="addRoomViewInfo.feeCoefficient"
                :placeholder="$t('addRoomView.requiredFeeCoefficient')"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addRoomView.roomStatus')">
              <el-select v-model="addRoomViewInfo.state" placeholder="必填，请选择房屋状态" style="width:100%">
                <el-option value="2002" :label="$t('addRoomView.notSold')"></el-option>
                <el-option value="2001" :label="$t('addRoomView.living')"></el-option>
                <el-option value="2003" :label="$t('addRoomView.delivered')"></el-option>
                <el-option value="2005" :label="$t('addRoomView.decorated')"></el-option>
                <el-option value="2004" :label="$t('addRoomView.notLived')"></el-option>
                <el-option value="2009" :label="$t('addRoomView.decorating')"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20" v-if="addRoomViewInfo.state != '2002'">
          <el-col :span="12">
            <el-form-item :label="$t('addRoomView.owner')">
              <el-input v-model="addRoomViewInfo.ownerName" disabled
                :placeholder="$t('addRoomView.requiredOwner')"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" size="small" @click="_openChooseOwner">{{ $t('addRoomView.selectOwner')
            }}</el-button>
          </el-col>
        </el-row>

        <div v-for="(item, index) in addRoomViewInfo.attrs" :key="index">
          <el-row :gutter="20" v-if="item.specType == '2233'">
            <el-col :span="12">
              <el-form-item :label="item.specName">
                <el-input v-model="item.value" :placeholder="item.specHoldplace"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20" v-if="item.specType == '3344'">
            <el-col :span="12">
              <el-form-item :label="item.specName">
                <el-select v-model="item.value" :placeholder="item.specHoldplace" style="width:100%">
                  <el-option v-for="value in item.values" :key="value.value" :label="value.valueName"
                    :value="value.value"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addRoomView.remark')">
              <el-input type="textarea" :placeholder="$t('addRoomView.optionalRemark')"
                v-model="addRoomViewInfo.remark"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <div class="text-right" style="margin-top:20px">
      <el-button type="warning" style="margin-right:20px;" @click="_goBack">
        {{ $t('common.back') }}
      </el-button>
      <el-button type="primary" @click="saveAddRoomInfo">
        <i class="el-icon-check"></i>{{ $t('common.submit') }}
      </el-button>
    </div>

    <search-owner ref="searchOwner" @chooseOwner="chooseOwner"></search-owner>
  </div>
</template>

<script>
import { saveRoom, queryFloors, queryUnits } from '@/api/room/addRoomViewApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import SearchOwner from '@/components/owner/SearchOwner'

export default {
  name: 'AddRoomViewList',
  components: { SearchOwner },
  data() {
    return {
      addRoomViewInfo: {
        roomNum: '',
        layer: '',
        section: '0',
        apartment: '10102',
        builtUpArea: '',
        feeCoefficient: '1.00',
        state: '2002',
        remark: '',
        roomSubType: '110',
        roomArea: '',
        roomRent: '0',
        unitPrice: '0',
        floorId: '',
        unitId: '',
        ownerId: '',
        ownerName: '',
        communityId: '',
        attrs: [],
        roomSubTypes: [],
        floors: [],
        units: []
      }
    }
  },
 async created() {
    this.addRoomViewInfo.communityId = getCommunityId()
    this.addRoomViewInfo.floorId = this.$route.query.floorId
    this._loadRoomAttrSpec()
    this._loadDictData()
    this._loadFloor()
    if(this.addRoomViewInfo.floorId){
      await this._loadUnit()
      this.addRoomViewInfo.unitId = this.$route.query.unitId
    }
  },
  methods: {
    async _loadDictData() {
      try {
        const data = await getDict('building_room', 'room_sub_type')
        this.addRoomViewInfo.roomSubTypes = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async _loadRoomAttrSpec() {
      try {
        this.addRoomViewInfo.attrs = []
        const data = await getDict('building_room_attr', '')
        data.forEach(item => {
          item.value = ''
          if (item.specShow == 'Y') {
            item.values = []
            this._loadAttrValue(item.specCd, item.values)
            this.addRoomViewInfo.attrs.push(item)
          }
        })
      } catch (error) {
        console.error('获取属性规格失败:', error)
      }
    },
    async _loadAttrValue(specCd, values) {
      try {
        const data = await getDict('attr_value', specCd)
        data.forEach(item => {
          if (item.valueShow == 'Y') {
            values.push(item)
          }
        })
      } catch (error) {
        console.error('获取属性值失败:', error)
      }
    },
    addRoomValidate() {
      const rules = {
        'addRoomViewInfo.roomNum': [
          { required: true, message: this.$t('addRoomView.validate.roomNumRequired') },
          { max: 64, message: this.$t('addRoomView.validate.roomNumMaxLength') }
        ],
        'addRoomViewInfo.layer': [
          { required: true, message: this.$t('addRoomView.validate.layerRequired') }
        ],
        'addRoomViewInfo.builtUpArea': [
          { required: true, message: this.$t('addRoomView.validate.builtUpAreaRequired') },
          { pattern: /^\d+(\.\d{1,2})?$/, message: this.$t('addRoomView.validate.builtUpAreaFormat') }
        ],
        'addRoomViewInfo.roomArea': [
          { required: true, message: this.$t('addRoomView.validate.roomAreaRequired') },
          { pattern: /^\d+(\.\d{1,2})?$/, message: this.$t('addRoomView.validate.roomAreaFormat') }
        ],
        'addRoomViewInfo.feeCoefficient': [
          { required: true, message: this.$t('addRoomView.validate.feeCoefficientRequired') },
          { pattern: /^\d+(\.\d{1,2})?$/, message: this.$t('addRoomView.validate.feeCoefficientFormat') }
        ],
        'addRoomViewInfo.state': [
          { required: true, message: this.$t('addRoomView.validate.stateRequired') },
          { max: 12, message: this.$t('addRoomView.validate.stateMaxLength') }
        ],
        'addRoomViewInfo.roomSubType': [
          { required: true, message: this.$t('addRoomView.validate.roomSubTypeRequired') }
        ],
        'addRoomViewInfo.remark': [
          { max: 200, message: this.$t('addRoomView.validate.remarkMaxLength') }
        ]
      }

      for (const field in rules) {
        const value = this._getNestedValue(field)
        const fieldRules = rules[field]

        for (const rule of fieldRules) {
          if (rule.required && (value === undefined || value === null || value === '')) {
            this.$message.error(rule.message)
            return false
          }
          if (rule.max && value && value.length > rule.max) {
            this.$message.error(rule.message)
            return false
          }
          if (rule.pattern && value && !rule.pattern.test(value)) {
            this.$message.error(rule.message)
            return false
          }
        }
      }

      return true
    },
    _getNestedValue(path) {
      return path.split('.').reduce((obj, key) => (obj ? obj[key] : undefined), this)
    },
    async saveAddRoomInfo() {
      // 验证必填属性
      let msg = ''
      this.addRoomViewInfo.attrs.forEach((item) => {
        if (item.required == 'Y' && !item.value) {
          msg = item.specHoldplace
        }
      })
      if (msg) {
        this.$message.error(msg)
        return
      }

      // 表单验证
      if (!this.addRoomValidate()) return

      try {
        await saveRoom(this.addRoomViewInfo)
        this.$message.success(this.$t('common.operationSuccess'))
        this._goBack()
      } catch (error) {
        console.error('保存失败:', error)
        this.$message.error(error.message || this.$t('addRoomView.saveError'))
      }
    },
    _goBack() {
      this.$router.go(-1)
    },
    _openChooseOwner() {
      this.$refs.searchOwner.open()
    },
    chooseOwner(owner) {
      this.addRoomViewInfo.ownerName = owner.name
      this.addRoomViewInfo.ownerId = owner.ownerId
    },
    async _loadFloor() {
      try {
        const params = {
          page: 1,
          row: 300,
          communityId: this.addRoomViewInfo.communityId
        }
        const data = await queryFloors(params)
        this.addRoomViewInfo.floors = data.apiFloorDataVoList || []
      } catch (error) {
        console.error('获取楼栋信息失败:', error)
      }
    },
    async _loadUnit() {
      if (!this.addRoomViewInfo.floorId) return
      this.addRoomViewInfo.unitId=''
      try {
        const params = {
          page: 1,
          row: 300,
          communityId: this.addRoomViewInfo.communityId,
          floorId: this.addRoomViewInfo.floorId
        }
        const data = await queryUnits(params)
        this.addRoomViewInfo.units = data || []
      } catch (error) {
        console.error('获取单元信息失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.add-room-view-container {
  padding: 20px;
}

.text-right {
  text-align: right;
  margin-top: 20px;
}

.ibox-tools {
  position: absolute;
  right: 15px;
  top: 10px;
}
</style>