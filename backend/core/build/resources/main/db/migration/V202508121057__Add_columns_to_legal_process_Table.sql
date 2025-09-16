-- Add is_cybercrime, classification_level_1 and classification_level_2 columns to table legal_process

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'legal_process' AND column_name = 'is_cybercrime') THEN
        RAISE NOTICE 'The is_cybercrime column already exists in legal_process table !';
    ELSE
        ALTER TABLE legal_process ADD COLUMN is_cybercrime boolean;
        RAISE NOTICE 'The is_cybercrime column has been added to legal_process table !';
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'legal_process' AND column_name = 'classification_level_1') THEN
        RAISE NOTICE 'The classification_level_1 column already exists in legal_process table !';
    ELSE
        ALTER TABLE legal_process ADD COLUMN classification_level_1 varchar(255);
        RAISE NOTICE 'The classification_level_1 column has been added to legal_process table !';
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'legal_process' AND column_name = 'classification_level_2') THEN
        RAISE NOTICE 'The classification_level_2 column already exists in legal_process table !';
    ELSE
        ALTER TABLE legal_process ADD COLUMN classification_level_2 varchar(255);
        RAISE NOTICE 'The classification_level_2 column has been added to legal_process table !';
    END IF;
END $$;
