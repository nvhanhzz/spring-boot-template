-- V7: Create master_constant_type lookup table and convert string enums to int
-- =============================================================================

-- =============================================================================
-- MASTER CONSTANT TYPE (Lookup table for enum codes)
-- Extends BaseEntity pattern
-- =============================================================================
CREATE TABLE master_constant_type (
    id          UUID PRIMARY KEY,
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    created_by  UUID,
    updated_by  UUID,
    status      INT NOT NULL DEFAULT 1,
    
    code        INT NOT NULL,
    category    VARCHAR(50) NOT NULL,
    name        VARCHAR(100) NOT NULL,
    note        TEXT,
    color       VARCHAR(20),
    
    UNIQUE (category, code),
    UNIQUE (category, name)
);

-- =============================================================================
-- INSERT ENUM DATA
-- =============================================================================

-- EntityStatus
INSERT INTO master_constant_type (id, status, category, code, name, note) VALUES
(gen_random_uuid(), 1, 'EntityStatus', 1, 'ACTIVE', 'Đang hoạt động'),
(gen_random_uuid(), 1, 'EntityStatus', 2, 'INACTIVE', 'Không hoạt động'),
(gen_random_uuid(), 1, 'EntityStatus', 3, 'DELETED', 'Đã xóa');

-- DataType
INSERT INTO master_constant_type (id, status, category, code, name, note) VALUES
(gen_random_uuid(), 1, 'DataType', 1, 'FLOAT', 'Số thực 32-bit'),
(gen_random_uuid(), 1, 'DataType', 2, 'DOUBLE', 'Số thực 64-bit'),
(gen_random_uuid(), 1, 'DataType', 3, 'INT', 'Số nguyên 32-bit'),
(gen_random_uuid(), 1, 'DataType', 4, 'LONG', 'Số nguyên 64-bit'),
(gen_random_uuid(), 1, 'DataType', 5, 'BOOL', 'Boolean'),
(gen_random_uuid(), 1, 'DataType', 6, 'STRING', 'Chuỗi ký tự'),
(gen_random_uuid(), 1, 'DataType', 7, 'DATETIME', 'Ngày giờ');

-- AssetType
INSERT INTO master_constant_type (id, status, category, code, name, note) VALUES
(gen_random_uuid(), 1, 'AssetType', 1, 'IMAGE', 'Hình ảnh'),
(gen_random_uuid(), 1, 'AssetType', 2, 'ICON', 'Icon'),
(gen_random_uuid(), 1, 'AssetType', 3, 'ANIMATION', 'Animation');

-- ProtocolType
INSERT INTO master_constant_type (id, status, category, code, name, note) VALUES
(gen_random_uuid(), 1, 'ProtocolType', 1, 'ANALOG', 'Analog input'),
(gen_random_uuid(), 1, 'ProtocolType', 2, 'MODBUS_RTU', 'Modbus RTU'),
(gen_random_uuid(), 1, 'ProtocolType', 3, 'MODBUS_TCP', 'Modbus TCP'),
(gen_random_uuid(), 1, 'ProtocolType', 4, 'OPC_UA', 'OPC UA'),
(gen_random_uuid(), 1, 'ProtocolType', 5, 'MQTT', 'MQTT');

-- TransformType
INSERT INTO master_constant_type (id, status, category, code, name, note) VALUES
(gen_random_uuid(), 1, 'TransformType', 1, 'NONE', 'Không transform'),
(gen_random_uuid(), 1, 'TransformType', 2, 'SCALE', 'Scale và offset'),
(gen_random_uuid(), 1, 'TransformType', 3, 'COMBINE_INT32', 'Ghép 2 registers thành INT32'),
(gen_random_uuid(), 1, 'TransformType', 4, 'COMBINE_FLOAT', 'Ghép 2 registers thành FLOAT32'),
(gen_random_uuid(), 1, 'TransformType', 5, 'FORMULA', 'Custom formula'),
(gen_random_uuid(), 1, 'TransformType', 6, 'BIT_EXTRACT', 'Lấy bit cụ thể');

-- ConditionType
INSERT INTO master_constant_type (id, status, category, code, name, note) VALUES
(gen_random_uuid(), 1, 'ConditionType', 1, 'EQ', 'Bằng'),
(gen_random_uuid(), 1, 'ConditionType', 2, 'NEQ', 'Khác'),
(gen_random_uuid(), 1, 'ConditionType', 3, 'GT', 'Lớn hơn'),
(gen_random_uuid(), 1, 'ConditionType', 4, 'GTE', 'Lớn hơn hoặc bằng'),
(gen_random_uuid(), 1, 'ConditionType', 5, 'LT', 'Nhỏ hơn'),
(gen_random_uuid(), 1, 'ConditionType', 6, 'LTE', 'Nhỏ hơn hoặc bằng'),
(gen_random_uuid(), 1, 'ConditionType', 7, 'BETWEEN', 'Trong khoảng'),
(gen_random_uuid(), 1, 'ConditionType', 8, 'NOT_BETWEEN', 'Ngoài khoảng'),
(gen_random_uuid(), 1, 'ConditionType', 9, 'BIT_ON', 'Bit bật'),
(gen_random_uuid(), 1, 'ConditionType', 10, 'BIT_OFF', 'Bit tắt'),
(gen_random_uuid(), 1, 'ConditionType', 11, 'IN', 'Trong danh sách'),
(gen_random_uuid(), 1, 'ConditionType', 12, 'NOT_IN', 'Ngoài danh sách'),
(gen_random_uuid(), 1, 'ConditionType', 13, 'LIKE', 'Giống'),
(gen_random_uuid(), 1, 'ConditionType', 14, 'NOT_LIKE', 'Không giống'),
(gen_random_uuid(), 1, 'ConditionType', 15, 'IS_NULL', 'Null'),
(gen_random_uuid(), 1, 'ConditionType', 16, 'IS_NOT_NULL', 'Không null');

-- ActionType
INSERT INTO master_constant_type (id, status, category, code, name, note) VALUES
(gen_random_uuid(), 1, 'ActionType', 1, 'CREATE_EVENT', 'Tạo sự kiện'),
(gen_random_uuid(), 1, 'ActionType', 2, 'CHANGE_STATUS', 'Đổi trạng thái'),
(gen_random_uuid(), 1, 'ActionType', 3, 'CALL_API', 'Gọi API'),
(gen_random_uuid(), 1, 'ActionType', 4, 'SEND_NOTIFICATION', 'Gửi thông báo'),
(gen_random_uuid(), 1, 'ActionType', 5, 'EXECUTE_COMMAND', 'Thực thi lệnh');

-- CommandType
INSERT INTO master_constant_type (id, status, category, code, name, note) VALUES
(gen_random_uuid(), 1, 'CommandType', 1, 'MODBUS_WRITE', 'Ghi Modbus'),
(gen_random_uuid(), 1, 'CommandType', 2, 'DIGITAL_OUTPUT', 'Digital Output'),
(gen_random_uuid(), 1, 'CommandType', 3, 'MQTT_PUBLISH', 'MQTT Publish'),
(gen_random_uuid(), 1, 'CommandType', 4, 'SYSTEM_CMD', 'Lệnh hệ thống');

-- CommandStatus
INSERT INTO master_constant_type (id, status, category, code, name, note) VALUES
(gen_random_uuid(), 1, 'CommandStatus', 1, 'PENDING', 'Đang chờ'),
(gen_random_uuid(), 1, 'CommandStatus', 2, 'SENT', 'Đã gửi'),
(gen_random_uuid(), 1, 'CommandStatus', 3, 'SUCCESS', 'Thành công'),
(gen_random_uuid(), 1, 'CommandStatus', 4, 'FAILED', 'Thất bại'),
(gen_random_uuid(), 1, 'CommandStatus', 5, 'TIMEOUT', 'Hết thời gian');

-- EventSeverity
INSERT INTO master_constant_type (id, status, category, code, name, note, color) VALUES
(gen_random_uuid(), 1, 'EventSeverity', 1, 'INFO', 'Thông tin', '#2196F3'),
(gen_random_uuid(), 1, 'EventSeverity', 2, 'WARNING', 'Cảnh báo', '#FF9800'),
(gen_random_uuid(), 1, 'EventSeverity', 3, 'ALARM', 'Báo động', '#F44336'),
(gen_random_uuid(), 1, 'EventSeverity', 4, 'CRITICAL', 'Nghiêm trọng', '#9C27B0');

-- EventStatus
INSERT INTO master_constant_type (id, status, category, code, name, note) VALUES
(gen_random_uuid(), 1, 'EventStatus', 1, 'ACTIVE', 'Đang hoạt động'),
(gen_random_uuid(), 1, 'EventStatus', 2, 'ACKNOWLEDGED', 'Đã xác nhận'),
(gen_random_uuid(), 1, 'EventStatus', 3, 'RESOLVED', 'Đã xử lý');

-- MachineStatus
INSERT INTO master_constant_type (id, status, category, code, name, note, color) VALUES
(gen_random_uuid(), 1, 'MachineStatus', 1, 'RUNNING', 'Đang chạy', '#4CAF50'),
(gen_random_uuid(), 1, 'MachineStatus', 2, 'STOP', 'Dừng', '#9E9E9E'),
(gen_random_uuid(), 1, 'MachineStatus', 3, 'ERROR', 'Lỗi', '#F44336'),
(gen_random_uuid(), 1, 'MachineStatus', 4, 'SYSTEM_STATUS', 'Trạng thái hệ thống', '#2196F3');

-- =============================================================================
-- CONVERT STRING COLUMNS TO INT
-- =============================================================================

-- Helper function to map string to code
CREATE OR REPLACE FUNCTION get_enum_code(p_category VARCHAR, p_name VARCHAR) RETURNS INT AS $$
DECLARE
    v_code INT;
BEGIN
    SELECT code INTO v_code FROM master_constant_type WHERE category = p_category AND name = p_name;
    RETURN COALESCE(v_code, 1); -- Default to 1 if not found
END;
$$ LANGUAGE plpgsql;

-- locations.status
ALTER TABLE locations ADD COLUMN status_new INT;
UPDATE locations SET status_new = get_enum_code('EntityStatus', status);
ALTER TABLE locations DROP COLUMN status;
ALTER TABLE locations RENAME COLUMN status_new TO status;
ALTER TABLE locations ALTER COLUMN status SET NOT NULL;

-- factory_maps.status
ALTER TABLE factory_maps ADD COLUMN status_new INT;
UPDATE factory_maps SET status_new = get_enum_code('EntityStatus', status);
ALTER TABLE factory_maps DROP COLUMN status;
ALTER TABLE factory_maps RENAME COLUMN status_new TO status;
ALTER TABLE factory_maps ALTER COLUMN status SET NOT NULL;

-- machine_groups.status
ALTER TABLE machine_groups ADD COLUMN status_new INT;
UPDATE machine_groups SET status_new = get_enum_code('EntityStatus', status);
ALTER TABLE machine_groups DROP COLUMN status;
ALTER TABLE machine_groups RENAME COLUMN status_new TO status;
ALTER TABLE machine_groups ALTER COLUMN status SET NOT NULL;

-- machines.status
ALTER TABLE machines ADD COLUMN status_new INT;
UPDATE machines SET status_new = get_enum_code('EntityStatus', status);
ALTER TABLE machines DROP COLUMN status;
ALTER TABLE machines RENAME COLUMN status_new TO status;
ALTER TABLE machines ALTER COLUMN status SET NOT NULL;

-- parameters.status
ALTER TABLE parameters ADD COLUMN status_new INT;
UPDATE parameters SET status_new = get_enum_code('EntityStatus', status);
ALTER TABLE parameters DROP COLUMN status;
ALTER TABLE parameters RENAME COLUMN status_new TO status;
ALTER TABLE parameters ALTER COLUMN status SET NOT NULL;

-- parameters.data_type
ALTER TABLE parameters ADD COLUMN data_type_new INT;
UPDATE parameters SET data_type_new = get_enum_code('DataType', data_type);
ALTER TABLE parameters DROP COLUMN data_type;
ALTER TABLE parameters RENAME COLUMN data_type_new TO data_type;

-- visual_assets.status
ALTER TABLE visual_assets ADD COLUMN status_new INT;
UPDATE visual_assets SET status_new = get_enum_code('EntityStatus', status);
ALTER TABLE visual_assets DROP COLUMN status;
ALTER TABLE visual_assets RENAME COLUMN status_new TO status;
ALTER TABLE visual_assets ALTER COLUMN status SET NOT NULL;

-- visual_assets.asset_type
ALTER TABLE visual_assets ADD COLUMN asset_type_new INT;
UPDATE visual_assets SET asset_type_new = get_enum_code('AssetType', asset_type);
ALTER TABLE visual_assets DROP COLUMN asset_type;
ALTER TABLE visual_assets RENAME COLUMN asset_type_new TO asset_type;

-- data_points.status
ALTER TABLE data_points ADD COLUMN status_new INT;
UPDATE data_points SET status_new = get_enum_code('EntityStatus', status);
ALTER TABLE data_points DROP COLUMN status;
ALTER TABLE data_points RENAME COLUMN status_new TO status;
ALTER TABLE data_points ALTER COLUMN status SET NOT NULL;

-- data_points.protocol
ALTER TABLE data_points ADD COLUMN protocol_new INT;
UPDATE data_points SET protocol_new = get_enum_code('ProtocolType', protocol);
ALTER TABLE data_points DROP COLUMN protocol;
ALTER TABLE data_points RENAME COLUMN protocol_new TO protocol;

-- data_points.transform_type
ALTER TABLE data_points ADD COLUMN transform_type_new INT;
UPDATE data_points SET transform_type_new = get_enum_code('TransformType', COALESCE(transform_type, 'NONE'));
ALTER TABLE data_points DROP COLUMN transform_type;
ALTER TABLE data_points RENAME COLUMN transform_type_new TO transform_type;

-- data_point_configs.status
ALTER TABLE data_point_configs ADD COLUMN status_new INT;
UPDATE data_point_configs SET status_new = get_enum_code('EntityStatus', status);
ALTER TABLE data_point_configs DROP COLUMN status;
ALTER TABLE data_point_configs RENAME COLUMN status_new TO status;
ALTER TABLE data_point_configs ALTER COLUMN status SET NOT NULL;

-- data_point_configs.condition
ALTER TABLE data_point_configs ADD COLUMN condition_new INT;
UPDATE data_point_configs SET condition_new = get_enum_code('ConditionType', condition);
ALTER TABLE data_point_configs DROP COLUMN condition;
ALTER TABLE data_point_configs RENAME COLUMN condition_new TO condition;
ALTER TABLE data_point_configs ALTER COLUMN condition SET NOT NULL;

-- data_point_displays.status
ALTER TABLE data_point_displays ADD COLUMN status_new INT;
UPDATE data_point_displays SET status_new = get_enum_code('EntityStatus', status);
ALTER TABLE data_point_displays DROP COLUMN status;
ALTER TABLE data_point_displays RENAME COLUMN status_new TO status;
ALTER TABLE data_point_displays ALTER COLUMN status SET NOT NULL;

-- data_point_actions.status
ALTER TABLE data_point_actions ADD COLUMN status_new INT;
UPDATE data_point_actions SET status_new = get_enum_code('EntityStatus', status);
ALTER TABLE data_point_actions DROP COLUMN status;
ALTER TABLE data_point_actions RENAME COLUMN status_new TO status;
ALTER TABLE data_point_actions ALTER COLUMN status SET NOT NULL;

-- data_point_actions.action_type
ALTER TABLE data_point_actions ADD COLUMN action_type_new INT;
UPDATE data_point_actions SET action_type_new = get_enum_code('ActionType', action_type);
ALTER TABLE data_point_actions DROP COLUMN action_type;
ALTER TABLE data_point_actions RENAME COLUMN action_type_new TO action_type;
ALTER TABLE data_point_actions ALTER COLUMN action_type SET NOT NULL;

-- data_point_histories.status
ALTER TABLE data_point_histories ADD COLUMN status_new INT;
UPDATE data_point_histories SET status_new = get_enum_code('EntityStatus', status);
ALTER TABLE data_point_histories DROP COLUMN status;
ALTER TABLE data_point_histories RENAME COLUMN status_new TO status;
ALTER TABLE data_point_histories ALTER COLUMN status SET NOT NULL;

-- command_templates.status
ALTER TABLE command_templates ADD COLUMN status_new INT;
UPDATE command_templates SET status_new = get_enum_code('EntityStatus', status);
ALTER TABLE command_templates DROP COLUMN status;
ALTER TABLE command_templates RENAME COLUMN status_new TO status;
ALTER TABLE command_templates ALTER COLUMN status SET NOT NULL;

-- command_templates.type
ALTER TABLE command_templates ADD COLUMN type_new INT;
UPDATE command_templates SET type_new = get_enum_code('CommandType', type);
ALTER TABLE command_templates DROP COLUMN type;
ALTER TABLE command_templates RENAME COLUMN type_new TO type;
ALTER TABLE command_templates ALTER COLUMN type SET NOT NULL;

-- command_logs.status
ALTER TABLE command_logs ADD COLUMN status_new INT;
UPDATE command_logs SET status_new = get_enum_code('EntityStatus', status);
ALTER TABLE command_logs DROP COLUMN status;
ALTER TABLE command_logs RENAME COLUMN status_new TO status;
ALTER TABLE command_logs ALTER COLUMN status SET NOT NULL;

-- command_logs.command_status
ALTER TABLE command_logs ADD COLUMN command_status_new INT;
UPDATE command_logs SET command_status_new = get_enum_code('CommandStatus', COALESCE(command_status, 'PENDING'));
ALTER TABLE command_logs DROP COLUMN command_status;
ALTER TABLE command_logs RENAME COLUMN command_status_new TO command_status;

-- event_definitions.status
ALTER TABLE event_definitions ADD COLUMN status_new INT;
UPDATE event_definitions SET status_new = get_enum_code('EntityStatus', status);
ALTER TABLE event_definitions DROP COLUMN status;
ALTER TABLE event_definitions RENAME COLUMN status_new TO status;
ALTER TABLE event_definitions ALTER COLUMN status SET NOT NULL;

-- event_definitions.severity
ALTER TABLE event_definitions ADD COLUMN severity_new INT;
UPDATE event_definitions SET severity_new = get_enum_code('EventSeverity', COALESCE(severity, 'ALARM'));
ALTER TABLE event_definitions DROP COLUMN severity;
ALTER TABLE event_definitions RENAME COLUMN severity_new TO severity;

-- machine_status_histories.status
ALTER TABLE machine_status_histories ADD COLUMN status_new INT;
UPDATE machine_status_histories SET status_new = get_enum_code('EntityStatus', status);
ALTER TABLE machine_status_histories DROP COLUMN status;
ALTER TABLE machine_status_histories RENAME COLUMN status_new TO status;
ALTER TABLE machine_status_histories ALTER COLUMN status SET NOT NULL;

-- machine_status_histories.machine_status
ALTER TABLE machine_status_histories ADD COLUMN machine_status_new INT;
UPDATE machine_status_histories SET machine_status_new = get_enum_code('MachineStatus', machine_status);
ALTER TABLE machine_status_histories DROP COLUMN machine_status;
ALTER TABLE machine_status_histories RENAME COLUMN machine_status_new TO machine_status;
ALTER TABLE machine_status_histories ALTER COLUMN machine_status SET NOT NULL;

-- events.status
ALTER TABLE events ADD COLUMN status_new INT;
UPDATE events SET status_new = get_enum_code('EntityStatus', status);
ALTER TABLE events DROP COLUMN status;
ALTER TABLE events RENAME COLUMN status_new TO status;
ALTER TABLE events ALTER COLUMN status SET NOT NULL;

-- events.severity
ALTER TABLE events ADD COLUMN severity_new INT;
UPDATE events SET severity_new = get_enum_code('EventSeverity', severity);
ALTER TABLE events DROP COLUMN severity;
ALTER TABLE events RENAME COLUMN severity_new TO severity;
ALTER TABLE events ALTER COLUMN severity SET NOT NULL;

-- events.event_status
ALTER TABLE events ADD COLUMN event_status_new INT;
UPDATE events SET event_status_new = get_enum_code('EventStatus', COALESCE(event_status, 'ACTIVE'));
ALTER TABLE events DROP COLUMN event_status;
ALTER TABLE events RENAME COLUMN event_status_new TO event_status;

-- =============================================================================
-- CLEANUP
-- =============================================================================
DROP FUNCTION get_enum_code(VARCHAR, VARCHAR);
